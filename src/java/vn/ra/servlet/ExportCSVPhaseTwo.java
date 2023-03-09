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
import vn.ra.process.EncodeSOPIN;
import vn.ra.utility.Config;
import vn.ra.utility.ConfigLanguage;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.LoadParamSystem;

/**
 *
 * @author vanth
 */
public class ExportCSVPhaseTwo extends HttpServlet {
private static final org.apache.log4j.Logger logServlet = org.apache.log4j.Logger.getLogger(ExportCSVPhaseTwo.class);
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
                            case "exportprofilecontrollist": {
                                //<editor-fold defaultstate="collapsed" desc="exportprofilecontrollist">
                                Config conf = new Config();
                                ConfigLanguage confLanguage = new ConfigLanguage();
                                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
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
                                String sConfirmResign = (String) request.getSession(false).getAttribute("sessResignProfileCert");
                                String sSignedEnabled = (String) request.getSession(false).getAttribute("sessSignedEnabledProfileCert");
                                String strAlertAllTimes = (String) request.getSession(false).getAttribute("AlertAllTimeSProfileCert");
                                String ToControlDate = (String) request.getSession(false).getAttribute("sessToControlProfileCert");
                                String FromControlDate = (String) request.getSession(false).getAttribute("sessFromControlProfileCert");
                                String strAlertAllControl = (String) request.getSession(false).getAttribute("AlertAllControlProfileCert");
                                String sEnterpriseCert = "";
                                String sPersonalCert = "";
                                String isUIDCollection = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_UID_COLLECTION_DISPLAY_ENABLED);
                                if(isUIDCollection.equals("1")) {
                                    String sENTERPRISE_PREFIX = (String) request.getSession(false).getAttribute("sessENTERPRISE_PREFIXProfileCert");
                                    String sPERSONAL_PREFIX = (String) request.getSession(false).getAttribute("sessPERSONAL_PREFIXProfileCert");
                                    String sENTERPRISE_ID = (String) request.getSession(false).getAttribute("sessENTERPRISE_IDProfileCert");
                                    String sPERSONAL_ID = (String) request.getSession(false).getAttribute("sessPERSONAL_IDProfileCert");
                                    if(!"".equals(sENTERPRISE_ID)){
                                        sEnterpriseCert = sENTERPRISE_PREFIX + "%" + sENTERPRISE_ID+"%";
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
                                if("1".equals(strAlertAllTimes)) {
                                    ToReceiveDate = "";
                                    FromReceiveDate = "";
                                }
                                if("1".equals(strAlertAllControl)) {
                                    ToControlDate = "";
                                    FromControlDate = "";
                                } else {
                                    ToCreateDate = "";
                                    FromCreateDate = "";
                                }
                                String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                String SessUserID = "";
                                String pBRANCH_BENEFICIARY_ID = SessUserAgentID;
                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    pBRANCH_BENEFICIARY_ID=EscapeUtils.escapeHtmlSearch(BranchOffice);
                                }
                                CERTIFICATION[][] rsReportBranch = new CERTIFICATION[1][];
                                if(!"".equals(vSum)) {
                                    if(Integer.parseInt(vSum) > 0) {
                                        if("0".equals(ToCreateDate)){ToCreateDate = "";}
                                        if(!isUIDCollection.equals("1")) {
                                            String[] sUIDResult = new String[2];
                                            CommonReferServlet.collectFieldToUID(TAX_CODE, BUDGET_CODE, EscapeUtils.escapeHtmlSearch(DECISION),
                                                P_ID, PASSPORT, EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                            sEnterpriseCert = sUIDResult[0];
                                            sPersonalCert = sUIDResult[1];
                                        }
                                        com.S_BO_CERTIFICATION_BRIEF_LIST_EXPORT(COMPANY_NAME, PERSONAL_NAME, idCollectEnabled, EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            BranchOffice, idCheckCompensation, idCheckOverdue, SessUserID, loginLanguage,
                                            rsReportBranch, idCheckCommitEnabled, stateProfile,
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
                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                        sCellMST = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_ENTERPRISE_SPLIT, loginLanguage).trim();
                                    }
                                    String sCellCompany = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COMPANY_NAME, loginLanguage).trim();
                                    String sCellCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_PERSONAL, loginLanguage).trim();
                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)){
                                        sCellCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_PERSONAL_SPLIT, loginLanguage).trim();
                                    }
                                    String sCellPesonal = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PERSONAL_NAME, loginLanguage).trim();
                                    String sCellProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SERVICEPACK_NAME, loginLanguage).trim();
                                    String sCellRequestType = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REQUEST_TYPE, loginLanguage).trim();
                                    String sCellStateCert = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_STATUS, loginLanguage);
                                    String sCellCertSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage);
                                    String sCellTokenSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_SN, loginLanguage).trim();
                                    String sCellMonthControl = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SERVICE_CHECKIN_MONTH, loginLanguage);
                                    String sCellStateProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_STATUS, loginLanguage);
                                    String sCellRecordProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_FORM_SEIZE, loginLanguage);
//                                    String sCellTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_TYPE, loginLanguage);
                                    String sCellProfileLegalStatus = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_LEGAL_STATUS, loginLanguage);
                                    String sCellControlState = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_CONTROL_STATUS, loginLanguage);
                                    String sCellDateControlProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_CONTROL_MONTH, loginLanguage);
                                    String sCellAmountFine = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_FINE_AMOUNT, loginLanguage);
                                    String sCellDateReceive = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_PROFILE_RECEIVE, loginLanguage);
                                    String sCelCheckRegister = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REGISTER_USE, loginLanguage);
                                    String sCelCheckConfirm = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERTIFICATION, loginLanguage);
                                    String sCelCheckGPKD = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REGISTER_BUSSINESS, loginLanguage);
                                    String sCelCheckCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_IDENTITY_CARD, loginLanguage);
                                    String sCelCheckStatusBusiness = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_BUSINESS_STATUS_CARD, loginLanguage);
                                    String sCelCheckStatusCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_IDENTITY_STATUS_CARD, loginLanguage);
                                    String sCelTokenState = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_STATUS, loginLanguage);
                                    String sCelTokenWaitLock = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_WAIT_LOCK, loginLanguage);
                                    String sCelTokenWaitUnlock = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_WAIT_UNLOCK, loginLanguage);
                                    String sCelCertEffective = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EFFECTIVE, loginLanguage);
                                    String sCelCertExpire = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EXPIRATION, loginLanguage);
                                    String sCelReasonRevoke = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REASON_REVOKE, loginLanguage);
                                    String sCelNoteProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_NOTE_PROFILE, loginLanguage);
                                    
                                    SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                    wb.setCompressTempFiles(true);
                                    CreationHelper createHelper = wb.getCreationHelper();
                                    
                                    //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - HEADER">
                                    SXSSFSheet sheet = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOTAL, loginLanguage));
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
                                    cellSyntheticTitle.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_NATIONAL_REGIME, loginLanguage));
                                    my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                    cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                    m = m+1;
                                    rowSyntheticTitle = sheet.createRow(m);
                                    sheet.addMergedRegion(new CellRangeAddress(m, m, 0, 8));
                                    cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                    cellSyntheticTitle.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_NATIONAL_BANNER, loginLanguage));
                                    my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                    cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                    m = m+1;
                                    rowSyntheticTitle = sheet.createRow(m);
                                    sheet.addMergedRegion(new CellRangeAddress(m, m, 6, 8));
                                    cellSyntheticTitle = rowSyntheticTitle.createCell((short) 5);
                                    cellSyntheticTitle.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_PLACE_SIGN, loginLanguage));
                                    my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                    cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                    m = m+1;
                                    rowSyntheticTitle = sheet.createRow(m);
                                    sheet.addMergedRegion(new CellRangeAddress(m, m, 0, 8));
                                    cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                    cellSyntheticTitle.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_MONTH_CONTROL_DEVELOPMENT, loginLanguage) + " " + ToCreateDate.trim() + "/" + FromCreateDate.trim());
                                    my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                    cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                    //</editor-fold>
                                    
                                    //<editor-fold defaultstate="collapsed" desc="### Sheet 2">
                                    SXSSFSheet sheet02 = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_CONTROL, loginLanguage));
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
//                                    sheet02.setColumnWidth(24, 18 * 255);
//                                    sheet02.setColumnWidth(25, 18 * 255);
                                    sheet02.setColumnWidth(24, 18 * 255);
                                    sheet02.setColumnWidth(25, 24 * 255);
                                    sheet02.setColumnWidth(26, 24 * 255);
                                    sheet02.setColumnWidth(27, 24 * 255);
                                    sheet02.setColumnWidth(28, 24 * 255);
                                    sheet02.setColumnWidth(29, 32 * 255);
                                    sheet02.setColumnWidth(30, 32 * 255);
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
                                    cell14.setCellValue(sCellProfileLegalStatus);
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
                                    
                                    /*Cell cellStatusBusiness = row.createCell((short) 24);
                                    cellStatusBusiness.setCellValue(sCelCheckStatusBusiness);
                                    cellStatusBusiness.setCellStyle(my_style);
                                    
                                    Cell cellStatusCMND = row.createCell((short) 25);
                                    cellStatusCMND.setCellValue(sCelCheckStatusCMND);
                                    cellStatusCMND.setCellStyle(my_style);*/
                                    
                                    Cell cell27 = row.createCell((short) 24);
                                    cell27.setCellValue(sCelTokenState);
                                    cell27.setCellStyle(my_style);
                                    
                                    Cell cell28 = row.createCell((short) 25);
                                    cell28.setCellValue(sCelTokenWaitLock);
                                    cell28.setCellStyle(my_style);
                                    
                                    Cell cell278 = row.createCell((short) 26);
                                    cell278.setCellValue(sCelTokenWaitUnlock);
                                    cell278.setCellStyle(my_style);
                                    
                                    Cell cellEffective = row.createCell((short) 27);
                                    cellEffective.setCellValue(sCelCertEffective);
                                    cellEffective.setCellStyle(my_style);
                                    
                                    Cell cellExpire = row.createCell((short) 28);
                                    cellExpire.setCellValue(sCelCertExpire);
                                    cellExpire.setCellStyle(my_style);
                                    
                                    Cell cellReason = row.createCell((short) 29);
                                    cellReason.setCellValue(sCelReasonRevoke);
                                    cellReason.setCellStyle(my_style);
                                    
                                    Cell cellNote = row.createCell((short) 30);
                                    cellNote.setCellValue(sCelNoteProfile);
                                    cellNote.setCellStyle(my_style);
                                    
                                    Font fontBranch = wb.createFont();
                                    fontBranch.setFontName("Arial");
                                    CellStyle styleText = wb.createCellStyle();
                                    styleText.setFont(fontBranch);
                                    styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));
                                    
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
                                            cellBranch1.setCellValue(rsReportBranch1.BRANCH_DESC);
                                            cellBranch1.setCellStyle(my_styleBranch);

                                            Cell cellBranch2 = row1.createCell((short) 2);
                                            cellBranch2.setCellValue(rsReportBranch1.CREATED_BY);
                                            cellBranch2.setCellStyle(my_styleBranch);

                                            String sMSTAndBUDGET_CODE = rsReportBranch1.ENTERPRISE_ID;
                                            String sP_IDAndPASSPORT = rsReportBranch1.PERSONAL_ID;
                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                sMSTAndBUDGET_CODE = CommonReferServlet.filterPrefixUIDLanguage(rsReportBranch1.ENTERPRISE_ID, loginLanguage);
                                                sP_IDAndPASSPORT = CommonReferServlet.filterPrefixUIDLanguage(rsReportBranch1.PERSONAL_ID, loginLanguage);
                                            }
                                            Cell cellBranch3 = row1.createCell((short) 3);
                                            cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                            cellBranch3.setCellStyle(styleText);

                                            Cell cellBranch5 = row1.createCell((short) 4);
                                            cellBranch5.setCellValue(rsReportBranch1.COMPANY_NAME);
                                            cellBranch5.setCellStyle(my_styleBranch);

                                            Cell cellBranch6 = row1.createCell((short) 5);
                                            cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                            cellBranch6.setCellStyle(styleText);

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

                                            String sRecordProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_OF_MONTH, loginLanguage);
                                            if(rsReportBranch1.BRIEF_TYPE == true) {
                                                sRecordProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_COMPENSATION, loginLanguage);
                                            }

                                            Cell cellBranch13 = row1.createCell((short) 12);
                                            cellBranch13.setCellValue(sRecordProfile);
                                            cellBranch13.setCellStyle(my_styleBranch);

                                            String sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SOFT_VERSION, loginLanguage);
                                            if(rsReportBranch1.COLLECT_SOFTCOPY == true) {
                                                sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_HARD_VERSION, loginLanguage);
                                            }

                                            Cell cellBranch14123 = row1.createCell((short) 13);
                                            cellBranch14123.setCellValue(sTypeProfile);
                                            cellBranch14123.setCellStyle(my_styleBranch);

                                            String sCOLLECT_ENABLED = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_CHECKED, loginLanguage);
                                            if(rsReportBranch1.COLLECT_ENABLED == false) {
                                                sCOLLECT_ENABLED = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_NOT_CONTROL, loginLanguage);
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
                                            cellBranch1612.setCellStyle(styleText);
                                            
                                            Cell cellBranch1613 = row1.createCell((short) 19);
                                            cellBranch1613.setCellValue(rsReportBranch1.TOKEN_SN);
                                            cellBranch1613.setCellStyle(styleText);

                                            String sRegister = "";
                                            String sConfirm = "";
                                            String sDKKD = "";
                                            String sCMND = "";
//                                            String sStatusDKKD = "";
//                                            String sStatusCMND = "";
                                            String sBRIEF_PROPERTIES = rsReportBranch1.BRIEF_PROPERTIES;
                                            if(!"".equals(sBRIEF_PROPERTIES)) {
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
//                                                    boolean bStatusDKKD = CommonFunction.checkBriefFileScan(Definitions.CONFIG_FILE_PROFILE_PHOTO_ACTIVITY_DECLARATION, resIPData);
//                                                    if(bStatusDKKD == true){sStatusDKKD = "x";}
//                                                    boolean bStatusCMND = CommonFunction.checkBriefFileScan(Definitions.CONFIG_FILE_PROFILE_PHOTO_ID_CARD, resIPData);
//                                                    if(bStatusCMND == true){sStatusCMND = "x";}
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
                                            
                                            Cell cellBranch27 = row1.createCell((short) 24);
                                            cellBranch27.setCellValue(rsReportBranch1.TOKEN_STATE_DESC);
                                            cellBranch27.setCellStyle(my_styleBranch);

                                            Cell cellBranch28 = row1.createCell((short) 25);
                                            cellBranch28.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_LOCK) ? "x" : "");
                                            cellBranch28.setCellStyle(my_styleBranch);

                                            Cell cellBranch29 = row1.createCell((short) 26);
                                            cellBranch29.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_UNLOCK) ? "x" : "");
                                            cellBranch29.setCellStyle(my_styleBranch);
                                            
                                            Cell valueEffective = row1.createCell((short) 27);
                                            Date dateEffective = CommonFunction.convertStringToDate(rsReportBranch1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                            if(dateEffective != null) {
                                                valueEffective.setCellValue(dateEffective);
                                            } else {
                                                valueEffective.setCellValue("");
                                            }
                                            valueEffective.setCellStyle(my_styleBranchDate);
                                            
                                            Cell valueExpire = row1.createCell((short) 28);
                                            Date dateExpire = CommonFunction.convertStringToDate(rsReportBranch1.EXPIRATION_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                            if(dateExpire != null) {
                                                valueExpire.setCellValue(dateExpire);
                                            } else {
                                                valueExpire.setCellValue("");
                                            }
                                            valueExpire.setCellStyle(my_styleBranchDate);
                                            
                                            Cell valueReason = row1.createCell((short) 29);
                                            valueReason.setCellValue(rsReportBranch1.REVOCATION_REASON);
                                            valueReason.setCellStyle(my_styleBranch);
                                            
                                            Cell valueNote = row1.createCell((short) 30);
                                            valueNote.setCellValue(rsReportBranch1.PROFILE_NOTE);
                                            valueNote.setCellStyle(my_styleBranch);
                                            
                                            i++;
                                        }
                                    }
                                    //</editor-fold>
                                    
                                    //<editor-fold defaultstate="collapsed" desc="### Sheet 3">
                                    SXSSFSheet sheet03 = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_MONTH_LACK, loginLanguage));
                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                        sheet03.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                        sheet03.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
                                        sheet03.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                        sheet03.setColumnWidth(3, 18 * 255);
                                        sheet03.setColumnWidth(4, 24 * 255);
                                        sheet03.setColumnWidth(5, 28 * 255);
                                        sheet03.setColumnWidth(6, 24 * 255);
                                        sheet03.setColumnWidth(7, 24 * 255);
                                        sheet03.setColumnWidth(8, 24 * 255);
                                        sheet03.setColumnWidth(9, 24 * 255);
                                        sheet03.setColumnWidth(10, 24 * 255);
                                        sheet03.setColumnWidth(11, 24 * 255);
                                        sheet03.setColumnWidth(12, 24 * 255);
                                        sheet03.setColumnWidth(13, 24 * 255);
                                        sheet03.setColumnWidth(14, 18 * 255);
                                        sheet03.setColumnWidth(15, 18 * 255);
                                        sheet03.setColumnWidth(16, 18 * 255);
                                        sheet03.setColumnWidth(17, 18 * 255);
                                        sheet03.setColumnWidth(18, 18 * 255);
                                        sheet03.setColumnWidth(19, 18 * 255);
                                        sheet03.setColumnWidth(20, 28 * 255);
                                        sheet03.setColumnWidth(21, 28 * 255);
                                        sheet03.setColumnWidth(22, 28 * 255);
                                        sheet03.setColumnWidth(23, 28 * 255);
                                        sheet03.setColumnWidth(24, 28 * 255);
                                        sheet03.setColumnWidth(25, 28 * 255);
                                        sheet03.setColumnWidth(26, 28 * 255);
                                        sheet03.setColumnWidth(27, 28 * 255);
                                        sheet03.setColumnWidth(28, 28 * 255);
                                        sheet03.setColumnWidth(29, 28 * 255);
                                        sheet03.setColumnWidth(30, 28 * 255);
                                        sheet03.setColumnWidth(31, 28 * 255);
                                        sheet03.setColumnWidth(32, 28 * 255);
                                        sheet03.setColumnWidth(33, 28 * 255);
                                        sheet03.setColumnWidth(34, 28 * 255);
                                        sheet03.setColumnWidth(35, 32 * 255);
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

                                        Cell cell113 = row3.createCell((short) 3);
                                        cell113.setCellValue(sCellMonthControl);
                                        cell113.setCellStyle(my_style3);

                                        Cell cell333 = row3.createCell((short) 4);
                                        cell333.setCellValue(sCellMST);
                                        cell333.setCellStyle(my_style3);

                                        Cell cell53 = row3.createCell((short) 5);
                                        cell53.setCellValue(sCellCompany);
                                        cell53.setCellStyle(my_style3);

                                        Cell cell93 = row3.createCell((short) 6);
                                        cell93.setCellValue(sCellProfile);
                                        cell93.setCellStyle(my_style3);

                                        Cell cellEffective3 = row3.createCell((short) 7);
                                        cellEffective3.setCellValue(sCelCertEffective);
                                        cellEffective3.setCellStyle(my_style);

                                        Cell cellExpire3 = row3.createCell((short) 8);
                                        cellExpire3.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EXPIRATION_CONTRACT, loginLanguage));
                                        cellExpire3.setCellStyle(my_style);

                                        Cell cell1924 = row3.createCell((short) 9);
                                        cell1924.setCellValue(sCellCertSN);
                                        cell1924.setCellStyle(my_style3);

                                        Cell cell103 = row3.createCell((short) 10);
                                        cell103.setCellValue(sCellRequestType);
                                        cell103.setCellStyle(my_style3);

                                        Cell cell1032 = row3.createCell((short) 11);
                                        cell1032.setCellValue(sCellStateCert);
                                        cell1032.setCellStyle(my_style3);

                                        Cell cell123 = row3.createCell((short) 12);
                                        cell123.setCellValue(sCellStateProfile);
                                        cell123.setCellStyle(my_style3);

                                        Cell cellStatusLegal = row3.createCell((short) 13);
                                        cellStatusLegal.setCellValue(sCellProfileLegalStatus);
                                        cellStatusLegal.setCellStyle(my_style3);
                                        
                                        Cell cellCheckRegister = row3.createCell((short) 14);
                                        cellCheckRegister.setCellValue(sCelCheckRegister);
                                        cellCheckRegister.setCellStyle(my_style3);
                                        
                                        Cell cellCheckConfirm = row3.createCell((short) 15);
                                        cellCheckConfirm.setCellValue(sCelCheckConfirm);
                                        cellCheckConfirm.setCellStyle(my_style3);
                                        
                                        Cell cellCheckGPKD = row3.createCell((short) 16);
                                        cellCheckGPKD.setCellValue(sCelCheckGPKD);
                                        cellCheckGPKD.setCellStyle(my_style3);
                                        
                                        Cell cellCheckStatusBusiness = row3.createCell((short) 17);
                                        cellCheckStatusBusiness.setCellValue(sCelCheckStatusBusiness);
                                        cellCheckStatusBusiness.setCellStyle(my_style3);
                                        
                                        Cell cellCheckCMND = row3.createCell((short) 18);
                                        cellCheckCMND.setCellValue(sCelCheckCMND);
                                        cellCheckCMND.setCellStyle(my_style3);
                                        
                                        Cell cellCheckStatusCMND = row3.createCell((short) 19);
                                        cellCheckStatusCMND.setCellValue(sCelCheckStatusCMND);
                                        cellCheckStatusCMND.setCellStyle(my_style3);
                                        
                                        Cell cellAddress = row3.createCell((short) 20);
                                        cellAddress.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_ADDRESS, loginLanguage));
                                        cellAddress.setCellStyle(my_style3);
                                        
                                        Cell cellRepresentative = row3.createCell((short) 21);
                                        cellRepresentative.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_NAME, loginLanguage));
                                        cellRepresentative.setCellStyle(my_style3);
                                        
                                        Cell cellPosition = row3.createCell((short) 22);
                                        cellPosition.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_POSITION, loginLanguage));
                                        cellPosition.setCellStyle(my_style3);
                                        
                                        Cell cellPhoneRepres = row3.createCell((short) 23);
                                        cellPhoneRepres.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_PHONE, loginLanguage));
                                        cellPhoneRepres.setCellStyle(my_style3);
                                        
                                        Cell cellEmailRepres = row3.createCell((short) 24);
                                        cellEmailRepres.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_EMAIL, loginLanguage));
                                        cellEmailRepres.setCellStyle(my_style3);
                                        
                                        Cell cellContactName = row3.createCell((short) 25);
                                        cellContactName.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CONTACT_NAME, loginLanguage));
                                        cellContactName.setCellStyle(my_style3);

                                        Cell cell63 = row3.createCell((short) 26);
                                        cell63.setCellValue(sCellCMND);
                                        cell63.setCellStyle(my_style3);

                                        Cell cell73 = row3.createCell((short) 27);
                                        cell73.setCellValue(sCellPesonal);
                                        cell73.setCellStyle(my_style3);

                                        Cell cell1931 = row3.createCell((short) 28);
                                        cell1931.setCellValue(sCellDateReceive);
                                        cell1931.setCellStyle(my_style3);

                                        Cell cell153 = row3.createCell((short) 29);
                                        cell153.setCellValue(sCellControlState);
                                        cell153.setCellStyle(my_style3);

                                        Cell cell193 = row3.createCell((short) 30);
                                        cell193.setCellValue(sCellAmountFine);
                                        cell193.setCellStyle(my_style3);

                                        Cell cellS1 = row3.createCell((short) 31);
                                        cellS1.setCellValue(sCelTokenState);
                                        cellS1.setCellStyle(my_style3);

                                        Cell cell1921 = row3.createCell((short) 32);
                                        cell1921.setCellValue(sCellTokenSN);
                                        cell1921.setCellStyle(my_style3);

                                        Cell cellS2 = row3.createCell((short) 33);
                                        cellS2.setCellValue(sCelTokenWaitLock);
                                        cellS2.setCellStyle(my_style3);

                                        Cell cellS3 = row3.createCell((short) 34);
                                        cellS3.setCellValue(sCelTokenWaitUnlock);
                                        cellS3.setCellStyle(my_style3);
                                        
                                        Cell cellNote35 = row3.createCell((short) 35);
                                        cellNote35.setCellValue(sCelNoteProfile);
                                        cellNote35.setCellStyle(my_style3);

                                        int ii = 1;
                                        int kk = 0;
                                        for (CERTIFICATION rsReportBranch1 : rsReportBranch[0]) {
                                            if(rsReportBranch1.COLLECT_ENABLED == false) {
                                                if (kk == 0) {
                                                    kk = 1;
                                                } else {
                                                    kk++;
                                                }
                                                Row row11 = sheet03.createRow(ii);
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                my_styleBranch.setFont(fontBranch);

                                                CellStyle my_styleBranchDate = wb.createCellStyle();
                                                Font fontBranchDate = wb.createFont();
                                                fontBranchDate.setFontName("Arial");
                                                my_styleBranchDate.setFont(fontBranchDate);
                                                my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));

                                                Cell cellBranch = row11.createCell((short) 0);
                                                cellBranch.setCellValue(kk);
                                                cellBranch.setCellStyle(my_styleBranch);

                                                Cell cellBranch1 = row11.createCell((short) 1);
                                                cellBranch1.setCellValue(rsReportBranch1.BRANCH_DESC);
                                                cellBranch1.setCellStyle(my_styleBranch);

                                                Cell cellBranch2 = row11.createCell((short) 2);
                                                cellBranch2.setCellValue(rsReportBranch1.CREATED_BY);
                                                cellBranch2.setCellStyle(my_styleBranch);

                                                Cell cellBranch11 = row11.createCell((short) 3);
                                                cellBranch11.setCellValue(rsReportBranch1.CROSS_CHECKED_MOUNTH);
                                                cellBranch11.setCellStyle(my_styleBranch);

                                                String sMSTAndBUDGET_CODE = CommonReferServlet.filterPrefixUIDLanguage(rsReportBranch1.ENTERPRISE_ID, loginLanguage);
                                                Cell cellBranch3 = row11.createCell((short) 4);
                                                cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                                cellBranch3.setCellStyle(styleText);

                                                Cell cellBranch5 = row11.createCell((short) 5);
                                                cellBranch5.setCellValue(rsReportBranch1.COMPANY_NAME);
                                                cellBranch5.setCellStyle(my_styleBranch);

                                                Cell cellBranch9 = row11.createCell((short) 6);
                                                cellBranch9.setCellValue(rsReportBranch1.CERTIFICATION_PROFILE_NAME);
                                                cellBranch9.setCellStyle(my_styleBranch);

                                                Cell valueEffective = row11.createCell((short) 7);
                                                Date dateEffective = CommonFunction.convertStringToDate(rsReportBranch1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateEffective != null) {
                                                    valueEffective.setCellValue(dateEffective);
                                                } else {
                                                    valueEffective.setCellValue("");
                                                }
                                                valueEffective.setCellStyle(my_styleBranchDate);

                                                Cell valueExpire = row11.createCell((short) 8);
                                                Date dateExpire = CommonFunction.convertStringToDate(rsReportBranch1.EXPIRATION_CONTRACT_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateExpire != null) {
                                                    valueExpire.setCellValue(dateExpire);
                                                } else {
                                                    valueExpire.setCellValue("");
                                                }
                                                valueExpire.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch1414 = row11.createCell((short) 9);
                                                cellBranch1414.setCellValue(rsReportBranch1.CERTIFICATION_SN);
                                                cellBranch1414.setCellStyle(styleText);
                                                
                                                Cell cellBranch10 = row11.createCell((short) 10);
                                                cellBranch10.setCellValue(rsReportBranch1.CERTIFICATION_ATTR_TYPE_DESC);
                                                cellBranch10.setCellStyle(my_styleBranch);

                                                Cell cellBranch101 = row11.createCell((short) 11);
                                                cellBranch101.setCellValue(rsReportBranch1.CERTIFICATION_STATE_DESC);
                                                cellBranch101.setCellStyle(my_styleBranch);

                                                Cell cellBranch12 = row11.createCell((short) 12);
                                                cellBranch12.setCellValue(rsReportBranch1.FILE_MANAGER_STATE_DESC);
                                                cellBranch12.setCellStyle(my_styleBranch);

                                                String sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SOFT_VERSION, loginLanguage);
                                                if(rsReportBranch1.COLLECT_SOFTCOPY == true) {
                                                    sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_HARD_VERSION, loginLanguage);
                                                }

                                                Cell cellBranch14123 = row11.createCell((short) 13);
                                                cellBranch14123.setCellValue(sTypeProfile);
                                                cellBranch14123.setCellStyle(my_styleBranch);

                                                String sRegister = "";
                                                String sConfirm = "";
                                                String sDKKD = "";
                                                String sCMND = "";
                                                String sStatusDKKD = "";
                                                String sStatusCMND = "";
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
                                                        boolean bStatusDKKD = CommonFunction.checkBriefFileScan(Definitions.CONFIG_FILE_PROFILE_PHOTO_ACTIVITY_DECLARATION, resIPData);
                                                        if(bStatusDKKD == true){sStatusDKKD = "x";}
                                                        boolean bStatusCMND = CommonFunction.checkBriefFileScan(Definitions.CONFIG_FILE_PROFILE_PHOTO_ID_CARD, resIPData);
                                                        if(bStatusCMND == true){sStatusCMND = "x";}
                                                    }
                                                }
                                                Cell cellBranch21 = row11.createCell((short) 14);
                                                cellBranch21.setCellValue(sRegister);
                                                cellBranch21.setCellStyle(my_styleBranch);

                                                Cell cellBranch22 = row11.createCell((short) 15);
                                                cellBranch22.setCellValue(sConfirm);
                                                cellBranch22.setCellStyle(my_styleBranch);

                                                Cell cellBranch23 = row11.createCell((short) 16);
                                                cellBranch23.setCellValue(sDKKD);
                                                cellBranch23.setCellStyle(my_styleBranch);

                                                Cell valueStatusBusiness = row11.createCell((short) 17);
                                                valueStatusBusiness.setCellValue(sStatusDKKD);
                                                valueStatusBusiness.setCellStyle(my_styleBranch);

                                                Cell cellBranch24 = row11.createCell((short) 18);
                                                cellBranch24.setCellValue(sCMND);
                                                cellBranch24.setCellStyle(my_styleBranch);

                                                Cell valueStatusCMND = row11.createCell((short) 19);
                                                valueStatusCMND.setCellValue(sStatusCMND);
                                                valueStatusCMND.setCellStyle(my_styleBranch);
                                                //
                                                String sADDRESS = "";
                                                String sPOSITION = "";
                                                String sREPRESENTATIVE_EMAIL = "";
                                                String sREPRESENTATIVE_PHONE = "";
                                                String sREPRESENTATIVE_NAME = "";
                                                String sCONTACT_NAME = "";
                                                String sPrfileContact = EscapeUtils.CheckTextNull(rsReportBranch1.PROFILE_CONTACT_INFO);
                                                if(!"".equals(sPrfileContact)) {
                                                    ObjectMapper oMapperParse = new ObjectMapper();
                                                    ProfileContactInfoJson profileContact = oMapperParse.readValue(sPrfileContact, ProfileContactInfoJson.class);
                                                    if(profileContact != null) {
                                                        sREPRESENTATIVE_EMAIL = EscapeUtils.CheckTextNull(profileContact.RepresentativeEmail);
                                                        sREPRESENTATIVE_PHONE = EscapeUtils.CheckTextNull(profileContact.RepresentativePhone);
                                                        sREPRESENTATIVE_NAME = CommonFunction.replaceCharaterSpecialJson(profileContact.RepresentativeName, false);
                                                        sCONTACT_NAME = CommonFunction.replaceCharaterSpecialJson(profileContact.ContactName, false);
                                                        sADDRESS = CommonFunction.replaceCharaterSpecialJson(profileContact.Address, false);
                                                        sPOSITION = CommonFunction.replaceCharaterSpecialJson(profileContact.Position, false);
                                                    }
                                                }
                                                Cell cellADDRESS = row11.createCell((short) 20);
                                                cellADDRESS.setCellValue(sADDRESS);
                                                cellADDRESS.setCellStyle(my_styleBranch);
                                                
                                                Cell cellREPRESENTATIVE_NAME = row11.createCell((short) 21);
                                                cellREPRESENTATIVE_NAME.setCellValue(sREPRESENTATIVE_NAME);
                                                cellREPRESENTATIVE_NAME.setCellStyle(my_styleBranch);
                                                
                                                Cell cellPOSITION = row11.createCell((short) 22);
                                                cellPOSITION.setCellValue(sPOSITION);
                                                cellPOSITION.setCellStyle(my_styleBranch);
                                                
                                                Cell cellREPRESENTATIVE_PHONE = row11.createCell((short) 23);
                                                cellREPRESENTATIVE_PHONE.setCellValue(sREPRESENTATIVE_PHONE);
                                                cellREPRESENTATIVE_PHONE.setCellStyle(my_styleBranch);
                                                
                                                Cell cellREPRESENTATIVE_EMAIL = row11.createCell((short) 24);
                                                cellREPRESENTATIVE_EMAIL.setCellValue(sREPRESENTATIVE_EMAIL);
                                                cellREPRESENTATIVE_EMAIL.setCellStyle(my_styleBranch);
                                                
                                                Cell cellCONTACT_NAME = row11.createCell((short) 25);
                                                cellCONTACT_NAME.setCellValue(sCONTACT_NAME);
                                                cellCONTACT_NAME.setCellStyle(my_styleBranch);
                                                //
                                                String sP_IDAndPASSPORT = CommonReferServlet.filterPrefixUIDLanguage(rsReportBranch1.PERSONAL_ID, loginLanguage);
                                                Cell cellBranch6 = row11.createCell((short) 26);
                                                cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                                cellBranch6.setCellStyle(styleText);

                                                Cell cellBranch7 = row11.createCell((short) 27);
                                                cellBranch7.setCellValue(rsReportBranch1.PERSONAL_NAME);
                                                cellBranch7.setCellStyle(my_styleBranch);

                                                Cell cellBranch1512 = row11.createCell((short) 28);
                                                Date dateReceive = CommonFunction.convertStringToDate(rsReportBranch1.RECEIVED_BRIEF_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateReceive != null) {
                                                    cellBranch1512.setCellValue(dateReceive);
                                                } else {
                                                    cellBranch1512.setCellValue("");
                                                }
                                                cellBranch1512.setCellStyle(my_styleBranchDate);
                                                
                                                String sCOLLECT_ENABLED = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_CHECKED, loginLanguage);
                                                if(rsReportBranch1.COLLECT_ENABLED == false) {
                                                    sCOLLECT_ENABLED = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_NOT_CONTROL, loginLanguage);
                                                }
                                                Cell cellBranch15222 = row11.createCell((short) 29);
                                                cellBranch15222.setCellValue(sCOLLECT_ENABLED);
                                                cellBranch15222.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch15 = row11.createCell((short) 30);
                                                cellBranch15.setCellValue(rsReportBranch1.FINE_FOR_LACK_OF_BRIEF);
                                                cellBranch15.setCellStyle(my_styleBranch);

                                                Cell cellBranch27 = row11.createCell((short) 31);
                                                cellBranch27.setCellValue(rsReportBranch1.TOKEN_STATE_DESC);
                                                cellBranch27.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch1412 = row11.createCell((short) 32);
                                                cellBranch1412.setCellValue(rsReportBranch1.TOKEN_SN);
                                                cellBranch1412.setCellStyle(styleText);
                                                
                                                Cell cellBranch28 = row11.createCell((short) 33);
                                                cellBranch28.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_LOCK) ? "x" : "");
                                                cellBranch28.setCellStyle(my_styleBranch);

                                                Cell cellBranch29 = row11.createCell((short) 34);
                                                cellBranch29.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_UNLOCK) ? "x" : "");
                                                cellBranch29.setCellStyle(my_styleBranch);
                                                
                                                Cell valueNote35 = row11.createCell((short) 35);
                                                valueNote35.setCellValue(rsReportBranch1.PROFILE_NOTE);
                                                valueNote35.setCellStyle(my_styleBranch);
                                                ii++;
                                            }
                                        }
                                    } else {
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
                                        sheet03.setColumnWidth(22, 18 * 255);
                                        sheet03.setColumnWidth(23, 18 * 255);
                                        sheet03.setColumnWidth(24, 18 * 255);
                                        sheet03.setColumnWidth(25, 24 * 255);
                                        sheet03.setColumnWidth(26, 24 * 255);
                                        sheet03.setColumnWidth(27, 24 * 255);
                                        sheet03.setColumnWidth(28, 24 * 255);
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
                                        cell143.setCellValue(sCellProfileLegalStatus);
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

                                        Cell cellStatusBusiness1 = row3.createCell((short) 22);
                                        cellStatusBusiness1.setCellValue(sCelCheckStatusBusiness);
                                        cellStatusBusiness1.setCellStyle(my_style);

                                        Cell cellStatusCMND1 = row3.createCell((short) 23);
                                        cellStatusCMND1.setCellValue(sCelCheckStatusCMND);
                                        cellStatusCMND1.setCellStyle(my_style);

                                        Cell cellS1 = row3.createCell((short) 24);
                                        cellS1.setCellValue(sCelTokenState);
                                        cellS1.setCellStyle(my_style3);

                                        Cell cellS2 = row3.createCell((short) 25);
                                        cellS2.setCellValue(sCelTokenWaitLock);
                                        cellS2.setCellStyle(my_style3);

                                        Cell cellS3 = row3.createCell((short) 26);
                                        cellS3.setCellValue(sCelTokenWaitUnlock);
                                        cellS3.setCellStyle(my_style3);

                                        Cell cellEffective3 = row3.createCell((short) 27);
                                        cellEffective3.setCellValue(sCelCertEffective);
                                        cellEffective3.setCellStyle(my_style);

                                        Cell cellExpire3 = row3.createCell((short) 28);
                                        cellExpire3.setCellValue(sCelCertExpire);
                                        cellExpire3.setCellStyle(my_style);

                                        int ii = 1;
                                        int kk = 0;
                                        for (CERTIFICATION rsReportBranch1 : rsReportBranch[0]) {
                                            if(rsReportBranch1.COLLECT_ENABLED == false) {
                                                if (kk == 0) {
                                                    kk = 1;
                                                } else {
                                                    kk++;
                                                }
                                                Row row11 = sheet03.createRow(ii);
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                my_styleBranch.setFont(fontBranch);

                                                CellStyle my_styleBranchDate = wb.createCellStyle();
                                                Font fontBranchDate = wb.createFont();
                                                fontBranchDate.setFontName("Arial");
                                                my_styleBranchDate.setFont(fontBranchDate);
                                                my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));

                                                Cell cellBranch = row11.createCell((short) 0);
                                                cellBranch.setCellValue(kk);
                                                cellBranch.setCellStyle(my_styleBranch);

                                                Cell cellBranch1 = row11.createCell((short) 1);
                                                cellBranch1.setCellValue(rsReportBranch1.BRANCH_DESC);
                                                cellBranch1.setCellStyle(my_styleBranch);

                                                Cell cellBranch2 = row11.createCell((short) 2);
                                                cellBranch2.setCellValue(rsReportBranch1.CREATED_BY);
                                                cellBranch2.setCellStyle(my_styleBranch);

                                                String sMSTAndBUDGET_CODE = rsReportBranch1.ENTERPRISE_ID;
                                                Cell cellBranch3 = row11.createCell((short) 3);
                                                cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                                cellBranch3.setCellStyle(styleText);

                                                Cell cellBranch5 = row11.createCell((short) 4);
                                                cellBranch5.setCellValue(rsReportBranch1.COMPANY_NAME);
                                                cellBranch5.setCellStyle(my_styleBranch);

                                                String sP_IDAndPASSPORT = rsReportBranch1.PERSONAL_ID;
                                                Cell cellBranch6 = row11.createCell((short) 5);
                                                cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                                cellBranch6.setCellStyle(styleText);

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

                                                String sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SOFT_VERSION, loginLanguage);
                                                if(rsReportBranch1.COLLECT_SOFTCOPY == true) {
                                                    sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_HARD_VERSION, loginLanguage);
                                                }

                                                Cell cellBranch14123 = row11.createCell((short) 12);
                                                cellBranch14123.setCellValue(sTypeProfile);
                                                cellBranch14123.setCellStyle(my_styleBranch);

                                                String sCOLLECT_ENABLED = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_CHECKED, loginLanguage);
                                                if(rsReportBranch1.COLLECT_ENABLED == false) {
                                                    sCOLLECT_ENABLED = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_NOT_CONTROL, loginLanguage);
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
                                                cellBranch1414.setCellStyle(styleText);

                                                Cell cellBranch1412 = row11.createCell((short) 17);
                                                cellBranch1412.setCellValue(rsReportBranch1.TOKEN_SN);
                                                cellBranch1412.setCellStyle(styleText);

                                                String sRegister = "";
                                                String sConfirm = "";
                                                String sDKKD = "";
                                                String sCMND = "";
                                                String sStatusDKKD = "";
                                                String sStatusCMND = "";
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
                                                        boolean bStatusDKKD = CommonFunction.checkBriefFileScan(Definitions.CONFIG_FILE_PROFILE_PHOTO_ACTIVITY_DECLARATION, resIPData);
                                                        if(bStatusDKKD == true){sStatusDKKD = "x";}
                                                        boolean bStatusCMND = CommonFunction.checkBriefFileScan(Definitions.CONFIG_FILE_PROFILE_PHOTO_ID_CARD, resIPData);
                                                        if(bStatusCMND == true){sStatusCMND = "x";}
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

                                                Cell valueStatusBusiness = row11.createCell((short) 22);
                                                valueStatusBusiness.setCellValue(sStatusDKKD);
                                                valueStatusBusiness.setCellStyle(my_styleBranch);

                                                Cell valueStatusCMND = row11.createCell((short) 23);
                                                valueStatusCMND.setCellValue(sStatusCMND);
                                                valueStatusCMND.setCellStyle(my_styleBranch);

                                                Cell cellBranch27 = row11.createCell((short) 24);
                                                cellBranch27.setCellValue(rsReportBranch1.TOKEN_STATE_DESC);
                                                cellBranch27.setCellStyle(my_styleBranch);

                                                Cell cellBranch28 = row11.createCell((short) 25);
                                                cellBranch28.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_LOCK) ? "x" : "");
                                                cellBranch28.setCellStyle(my_styleBranch);

                                                Cell cellBranch29 = row11.createCell((short) 26);
                                                cellBranch29.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_UNLOCK) ? "x" : "");
                                                cellBranch29.setCellStyle(my_styleBranch);

                                                Cell valueEffective = row11.createCell((short) 27);
                                                Date dateEffective = CommonFunction.convertStringToDate(rsReportBranch1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateEffective != null) {
                                                    valueEffective.setCellValue(dateEffective);
                                                } else {
                                                    valueEffective.setCellValue("");
                                                }
                                                valueEffective.setCellStyle(my_styleBranchDate);

                                                Cell valueExpire = row11.createCell((short) 28);
                                                Date dateExpire = CommonFunction.convertStringToDate(rsReportBranch1.EXPIRATION_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateExpire != null) {
                                                    valueExpire.setCellValue(dateExpire);
                                                } else {
                                                    valueExpire.setCellValue("");
                                                }
                                                valueExpire.setCellStyle(my_styleBranchDate);
                                                ii++;
                                            }
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
                            case "exportprofilecontrollist_20": {
                                //<editor-fold defaultstate="collapsed" desc="exportprofilecontrollist_20">
                                Config conf = new Config();
                                ConfigLanguage confLanguage = new ConfigLanguage();
                                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
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
                                String sConfirmResign = (String) request.getSession(false).getAttribute("sessResignProfileCert");
                                String sSignedEnabled = (String) request.getSession(false).getAttribute("sessSignedEnabledProfileCert");
                                String strAlertAllTimes = (String) request.getSession(false).getAttribute("AlertAllTimeSRenewCert");
                                String ToControlDate = (String) request.getSession(false).getAttribute("sessToControlProfileCert");
                                String FromControlDate = (String) request.getSession(false).getAttribute("sessFromControlProfileCert");
                                String strAlertAllControl = (String) request.getSession(false).getAttribute("AlertAllControlProfileCert");
                                String sEnterpriseCert = "";
                                String sPersonalCert = "";
                                String isUIDCollection = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_UID_COLLECTION_DISPLAY_ENABLED);
                                if(isUIDCollection.equals("1")) {
                                    String sENTERPRISE_PREFIX = (String) request.getSession(false).getAttribute("sessENTERPRISE_PREFIXProfileCert");
                                    String sPERSONAL_PREFIX = (String) request.getSession(false).getAttribute("sessPERSONAL_PREFIXProfileCert");
                                    String sENTERPRISE_ID = (String) request.getSession(false).getAttribute("sessENTERPRISE_IDProfileCert");
                                    String sPERSONAL_ID = (String) request.getSession(false).getAttribute("sessPERSONAL_IDProfileCert");
                                    if(!"".equals(sENTERPRISE_ID)){
                                        sEnterpriseCert = sENTERPRISE_PREFIX + sENTERPRISE_ID;
                                    }
                                    if(!"".equals(sPERSONAL_ID)){
                                        sPersonalCert = sPERSONAL_PREFIX + sPERSONAL_ID;
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
                                if("1".equals(strAlertAllTimes)) {
                                    ToReceiveDate = "";
                                    FromReceiveDate = "";
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
                                CERTIFICATION[][] rsReportBranch = new CERTIFICATION[1][];
                                if(!"".equals(vSum)) {
                                    if(Integer.parseInt(vSum) > 0) {
                                        if("0".equals(ToCreateDate)){ToCreateDate = "";}
                                        if(!isUIDCollection.equals("1")) {
                                            String[] sUIDResult = new String[2];
                                            CommonReferServlet.collectFieldToUID(TAX_CODE, BUDGET_CODE, EscapeUtils.escapeHtmlSearch(DECISION),
                                                P_ID, PASSPORT, EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                            sEnterpriseCert = sUIDResult[0];
                                            sPersonalCert = sUIDResult[1];
                                        }
                                        com.S_BO_CERTIFICATION_BRIEF_LIST_EXPORT(COMPANY_NAME, PERSONAL_NAME, idCollectEnabled, EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            BranchOffice, idCheckCompensation, idCheckOverdue, SessUserID, loginLanguage,
                                            rsReportBranch, idCheckCommitEnabled, stateProfile,
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
                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                        sCellMST = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_ENTERPRISE_SPLIT, loginLanguage).trim();
                                    }
                                    String sCellCompany = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COMPANY_NAME, loginLanguage).trim();
                                    String sCellCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_PERSONAL, loginLanguage).trim();
                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)){
                                        sCellCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_PERSONAL_SPLIT, loginLanguage).trim();
                                    }
                                    String sCellPesonal = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PERSONAL_NAME, loginLanguage).trim();
                                    String sCellProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SERVICEPACK_NAME, loginLanguage).trim();
                                    String sCellRequestType = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REQUEST_TYPE, loginLanguage).trim();
                                    String sCellStateCert = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_STATUS, loginLanguage);
                                    String sCellCertSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage);
                                    String sCellTokenSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_SN, loginLanguage).trim();
                                    String sCellMonthControl = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SERVICE_CHECKIN_MONTH, loginLanguage);
                                    String sCellStateProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_STATUS, loginLanguage);
                                    String sCellRecordProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_FORM_SEIZE, loginLanguage);
                                    String sCellTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_TYPE, loginLanguage);
                                    String sCellProfileLegalStatus = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_LEGAL_STATUS, loginLanguage);
                                    String sCellControlState = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_CONTROL_STATUS, loginLanguage);
                                    String sCellDateControlProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_CONTROL_MONTH, loginLanguage);
                                    String sCellAmountFine = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_FINE_AMOUNT, loginLanguage);
                                    String sCellDateReceive = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_PROFILE_RECEIVE, loginLanguage);
                                    String sCelCheckRegister = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REGISTER_USE, loginLanguage);
                                    String sCelCheckConfirm = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERTIFICATION, loginLanguage);
                                    String sCelCheckGPKD = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REGISTER_BUSSINESS, loginLanguage);
                                    String sCelCheckCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_IDENTITY_CARD, loginLanguage);
                                    String sCelCheckStatusBusiness = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_BUSINESS_STATUS_CARD, loginLanguage);
                                    String sCelCheckStatusCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_IDENTITY_STATUS_CARD, loginLanguage);
                                    String sCelTokenState = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_STATUS, loginLanguage);
                                    String sCelTokenWaitLock = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_WAIT_LOCK, loginLanguage);
                                    String sCelTokenWaitUnlock = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_WAIT_UNLOCK, loginLanguage);
                                    String sCelCertEffective = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EFFECTIVE, loginLanguage);
                                    String sCelCertExpire = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EXPIRATION, loginLanguage);
                                    
                                    SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                    wb.setCompressTempFiles(true);
                                    CreationHelper createHelper = wb.getCreationHelper();
                                    
                                    //<editor-fold defaultstate="collapsed" desc="### CLOSE SHEET TONG HOP - HEADER">
                                    /*SXSSFSheet sheet = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOTAL, loginLanguage));
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
                                    cellSyntheticTitle.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_NATIONAL_REGIME, loginLanguage));
                                    my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                    cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                    m = m+1;
                                    rowSyntheticTitle = sheet.createRow(m);
                                    sheet.addMergedRegion(new CellRangeAddress(m, m, 0, 8));
                                    cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                    cellSyntheticTitle.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_NATIONAL_BANNER, loginLanguage));
                                    my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                    cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                    m = m+1;
                                    rowSyntheticTitle = sheet.createRow(m);
                                    sheet.addMergedRegion(new CellRangeAddress(m, m, 6, 8));
                                    cellSyntheticTitle = rowSyntheticTitle.createCell((short) 5);
                                    cellSyntheticTitle.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_PLACE_SIGN, loginLanguage));
                                    my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                    cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                    m = m+1;
                                    rowSyntheticTitle = sheet.createRow(m);
                                    sheet.addMergedRegion(new CellRangeAddress(m, m, 0, 8));
                                    cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                    cellSyntheticTitle.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_MONTH_CONTROL_DEVELOPMENT, loginLanguage) + " " + ToCreateDate.trim() + "/" + FromCreateDate.trim());
                                    my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                    cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);*/
                                    //</editor-fold>
                                    
                                    //<editor-fold defaultstate="collapsed" desc="### Sheet 2">
                                    SXSSFSheet sheet02 = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_CONTROL, loginLanguage));
                                    sheet02.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                    sheet02.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
                                    sheet02.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                    sheet02.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                    sheet02.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                    sheet02.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                    sheet02.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                    sheet02.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                    sheet02.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                    sheet02.setColumnWidth(9, 24 * 255);
                                    sheet02.setColumnWidth(10, 18 * 255);
                                    sheet02.setColumnWidth(11, 18 * 255);
                                    sheet02.setColumnWidth(12, 18 * 255);
                                    sheet02.setColumnWidth(13, 18 * 255);
                                    sheet02.setColumnWidth(14, 18 * 255);
                                    sheet02.setColumnWidth(15, 24 * 255);
                                    sheet02.setColumnWidth(16, 18 * 255);
                                    sheet02.setColumnWidth(17, 24 * 255);
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

                                    Cell cell10 = row.createCell((short) 7);
                                    cell10.setCellValue(sCellRequestType);
                                    cell10.setCellStyle(my_style);

                                    Cell cell9 = row.createCell((short) 8);
                                    cell9.setCellValue(sCellProfile);
                                    cell9.setCellStyle(my_style);
                                    
                                    Cell cellEffective = row.createCell((short) 9);
                                    cellEffective.setCellValue(sCelCertEffective);
                                    cellEffective.setCellStyle(my_style);

                                    Cell cell20 = row.createCell((short) 10);
                                    cell20.setCellValue(sCellDateReceive);
                                    cell20.setCellStyle(my_style);

                                    Cell cell25 = row.createCell((short) 11);
                                    cell25.setCellValue(sCelCheckGPKD);
                                    cell25.setCellStyle(my_style);

                                    Cell cell26 = row.createCell((short) 12);
                                    cell26.setCellValue(sCelCheckCMND);
                                    cell26.setCellStyle(my_style);

                                    Cell cell23 = row.createCell((short) 13);
                                    cell23.setCellValue(sCelCheckRegister);
                                    cell23.setCellStyle(my_style);

                                    Cell cell24 = row.createCell((short) 14);
                                    cell24.setCellValue(sCelCheckConfirm);
                                    cell24.setCellStyle(my_style);

                                    Cell cell12 = row.createCell((short) 15);
                                    cell12.setCellValue(sCellStateProfile);
                                    cell12.setCellStyle(my_style);

                                    Cell cell14 = row.createCell((short) 16);
                                    cell14.setCellValue(sCellTypeProfile);
                                    cell14.setCellStyle(my_style);

                                    Cell cell16 = row.createCell((short) 17);
                                    cell16.setCellValue(sCellDateControlProfile);
                                    cell16.setCellStyle(my_style);
                                    //
                                    
//                                    Cell cell102 = row.createCell((short) 9);
//                                    cell102.setCellValue(sCellStateCert);
//                                    cell102.setCellStyle(my_style);
//
//                                    Cell cell11 = row.createCell((short) 10);
//                                    cell11.setCellValue(sCellMonthControl);
//                                    cell11.setCellStyle(my_style);
//
//                                    Cell cell13 = row.createCell((short) 12);
//                                    cell13.setCellValue(sCellRecordProfile);
//                                    cell13.setCellStyle(my_style);
//
//                                    Cell cell15 = row.createCell((short) 14);
//                                    cell15.setCellValue(sCellControlState);
//                                    cell15.setCellStyle(my_style);
//
//                                    Cell cell19 = row.createCell((short) 16);
//                                    cell19.setCellValue(sCellAmountFine);
//                                    cell19.setCellStyle(my_style);
//                                    
//                                    Cell cell203 = row.createCell((short) 18);
//                                    cell203.setCellValue(sCellCertSN);
//                                    cell203.setCellStyle(my_style);
//                                    
//                                    Cell cell212 = row.createCell((short) 19);
//                                    cell212.setCellValue(sCellTokenSN);
//                                    cell212.setCellStyle(my_style);
                                    
                                    /*Cell cellStatusBusiness = row.createCell((short) 24);
                                    cellStatusBusiness.setCellValue(sCelCheckStatusBusiness);
                                    cellStatusBusiness.setCellStyle(my_style);
                                    
                                    Cell cellStatusCMND = row.createCell((short) 25);
                                    cellStatusCMND.setCellValue(sCelCheckStatusCMND);
                                    cellStatusCMND.setCellStyle(my_style);*/
                                    
//                                    Cell cell27 = row.createCell((short) 24);
//                                    cell27.setCellValue(sCelTokenState);
//                                    cell27.setCellStyle(my_style);
//                                    
//                                    Cell cell28 = row.createCell((short) 25);
//                                    cell28.setCellValue(sCelTokenWaitLock);
//                                    cell28.setCellStyle(my_style);
//                                    
//                                    Cell cell278 = row.createCell((short) 26);
//                                    cell278.setCellValue(sCelTokenWaitUnlock);
//                                    cell278.setCellStyle(my_style);
//                                    
//                                    Cell cellExpire = row.createCell((short) 28);
//                                    cellExpire.setCellValue(sCelCertExpire);
//                                    cellExpire.setCellStyle(my_style);
                                    
                                    Font fontBranch = wb.createFont();
                                    fontBranch.setFontName("Arial");
                                    CellStyle styleText = wb.createCellStyle();
                                    styleText.setFont(fontBranch);
                                    styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));
                                    
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
                                            cellBranch1.setCellValue(rsReportBranch1.BRANCH_DESC);
                                            cellBranch1.setCellStyle(my_styleBranch);

                                            Cell cellBranch2 = row1.createCell((short) 2);
                                            cellBranch2.setCellValue(rsReportBranch1.CREATED_BY);
                                            cellBranch2.setCellStyle(my_styleBranch);

                                            String sMSTAndBUDGET_CODE = rsReportBranch1.ENTERPRISE_ID;
                                            String sP_IDAndPASSPORT = rsReportBranch1.PERSONAL_ID;
                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                sMSTAndBUDGET_CODE = CommonReferServlet.filterPrefixUIDLanguage(rsReportBranch1.ENTERPRISE_ID, loginLanguage);
                                                sP_IDAndPASSPORT = CommonReferServlet.filterPrefixUIDLanguage(rsReportBranch1.PERSONAL_ID, loginLanguage);
                                            }
                                            Cell cellBranch3 = row1.createCell((short) 3);
                                            cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                            cellBranch3.setCellStyle(styleText);

                                            Cell cellBranch5 = row1.createCell((short) 4);
                                            cellBranch5.setCellValue(rsReportBranch1.COMPANY_NAME);
                                            cellBranch5.setCellStyle(my_styleBranch);

                                            Cell cellBranch6 = row1.createCell((short) 5);
                                            cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                            cellBranch6.setCellStyle(styleText);

                                            Cell cellBranch7 = row1.createCell((short) 6);
                                            cellBranch7.setCellValue(rsReportBranch1.PERSONAL_NAME);
                                            cellBranch7.setCellStyle(my_styleBranch);

                                            Cell cellBranch10 = row1.createCell((short) 7);
                                            cellBranch10.setCellValue(rsReportBranch1.CERTIFICATION_ATTR_TYPE_DESC);
                                            cellBranch10.setCellStyle(my_styleBranch);

                                            Cell cellBranch9 = row1.createCell((short) 8);
                                            cellBranch9.setCellValue(rsReportBranch1.CERTIFICATION_PROFILE_NAME);
                                            cellBranch9.setCellStyle(my_styleBranch);
                                            
                                            Cell valueEffective = row1.createCell((short) 9);
                                            Date dateEffective = CommonFunction.convertStringToDate(rsReportBranch1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                            if(dateEffective != null) {
                                                valueEffective.setCellValue(dateEffective);
                                            } else {
                                                valueEffective.setCellValue("");
                                            }
                                            valueEffective.setCellStyle(my_styleBranchDate);

                                            Cell cellBranch16 = row1.createCell((short) 10);
                                            Date dateReceive = CommonFunction.convertStringToDate(rsReportBranch1.RECEIVED_BRIEF_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                            if(dateReceive != null) {
                                                cellBranch16.setCellValue(dateReceive);
                                            } else {
                                                cellBranch16.setCellValue("");
                                            }
                                            cellBranch16.setCellStyle(my_styleBranchDate);

                                            String sRegister = "";
                                            String sConfirm = "";
                                            String sDKKD = "";
                                            String sCMND = "";
                                            String sBRIEF_PROPERTIES = rsReportBranch1.BRIEF_PROPERTIES;
                                            if(!"".equals(sBRIEF_PROPERTIES)) {
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

                                            Cell cellBranch23 = row1.createCell((short) 11);
                                            cellBranch23.setCellValue(sDKKD);
                                            cellBranch23.setCellStyle(my_styleBranch);

                                            Cell cellBranch24 = row1.createCell((short) 12);
                                            cellBranch24.setCellValue(sCMND);
                                            cellBranch24.setCellStyle(my_styleBranch);
                                            Cell cellBranch21 = row1.createCell((short) 13);
                                            cellBranch21.setCellValue(sRegister);
                                            cellBranch21.setCellStyle(my_styleBranch);

                                            Cell cellBranch22 = row1.createCell((short) 14);
                                            cellBranch22.setCellValue(sConfirm);
                                            cellBranch22.setCellStyle(my_styleBranch);
                                            
                                            Cell cellBranch27 = row1.createCell((short) 15);
                                            cellBranch27.setCellValue(rsReportBranch1.TOKEN_STATE_DESC);
                                            cellBranch27.setCellStyle(my_styleBranch);

                                            String sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SOFT_VERSION, loginLanguage);
                                            if(rsReportBranch1.COLLECT_SOFTCOPY == true) {
                                                sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_HARD_VERSION, loginLanguage);
                                            }

                                            Cell cellBranch14123 = row1.createCell((short) 16);
                                            cellBranch14123.setCellValue(sTypeProfile);
                                            cellBranch14123.setCellStyle(my_styleBranch);

                                            Cell cellBranch11 = row1.createCell((short) 17);
                                            cellBranch11.setCellValue(rsReportBranch1.CROSS_CHECKED_MOUNTH);
                                            cellBranch11.setCellStyle(my_styleBranch);
                                            //
                                            
//                                            Cell cellBranch102 = row1.createCell((short) 9);
//                                            cellBranch102.setCellValue(rsReportBranch1.CERTIFICATION_STATE_DESC);
//                                            cellBranch102.setCellStyle(my_styleBranch);
//                                            
//                                            Cell cellBranch12 = row1.createCell((short) 11);
//                                            cellBranch12.setCellValue(rsReportBranch1.FILE_MANAGER_STATE_DESC);
//                                            cellBranch12.setCellStyle(my_styleBranch);
//
//                                            String sRecordProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_OF_MONTH, loginLanguage);
//                                            if(rsReportBranch1.BRIEF_TYPE == true) {
//                                                sRecordProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_COMPENSATION, loginLanguage);
//                                            }
//
//                                            Cell cellBranch13 = row1.createCell((short) 12);
//                                            cellBranch13.setCellValue(sRecordProfile);
//                                            cellBranch13.setCellStyle(my_styleBranch);
//
//                                            String sCOLLECT_ENABLED = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_CHECKED, loginLanguage);
//                                            if(rsReportBranch1.COLLECT_ENABLED == false) {
//                                                sCOLLECT_ENABLED = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_NOT_CONTROL, loginLanguage);
//                                            }
//                                            Cell cellBranch15222 = row1.createCell((short) 14);
//                                            cellBranch15222.setCellValue(sCOLLECT_ENABLED);
//                                            cellBranch15222.setCellStyle(my_styleBranch);
//
//                                            Cell cellBranch14 = row1.createCell((short) 15);
//                                            Date dateControls = CommonFunction.convertStringToDate(rsReportBranch1.COLLECTED_FULL_BRIEF_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
//                                            if(dateControls != null) {
//                                                cellBranch14.setCellValue(dateControls);
//                                            } else {
//                                                cellBranch14.setCellValue("");
//                                            }
//                                            cellBranch14.setCellStyle(my_styleBranchDate);
//
//                                            Cell cellBranch15 = row1.createCell((short) 16);
//                                            cellBranch15.setCellValue(rsReportBranch1.FINE_FOR_LACK_OF_BRIEF);
//                                            cellBranch15.setCellStyle(my_styleBranch);
//
//                                            Cell cellBranch1612 = row1.createCell((short) 18);
//                                            cellBranch1612.setCellValue(rsReportBranch1.CERTIFICATION_SN);
//                                            cellBranch1612.setCellStyle(styleText);
//                                            
//                                            Cell cellBranch1613 = row1.createCell((short) 19);
//                                            cellBranch1613.setCellValue(rsReportBranch1.TOKEN_SN);
//                                            cellBranch1613.setCellStyle(styleText);
//
//                                            Cell cellBranch28 = row1.createCell((short) 25);
//                                            cellBranch28.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_LOCK) ? "x" : "");
//                                            cellBranch28.setCellStyle(my_styleBranch);
//
//                                            Cell cellBranch29 = row1.createCell((short) 26);
//                                            cellBranch29.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_UNLOCK) ? "x" : "");
//                                            cellBranch29.setCellStyle(my_styleBranch);
//                                            
//                                            Cell valueExpire = row1.createCell((short) 28);
//                                            Date dateExpire = CommonFunction.convertStringToDate(rsReportBranch1.EXPIRATION_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
//                                            if(dateExpire != null) {
//                                                valueExpire.setCellValue(dateExpire);
//                                            } else {
//                                                valueExpire.setCellValue("");
//                                            }
//                                            valueExpire.setCellStyle(my_styleBranchDate);
                                            i++;
                                        }
                                    }
                                    //</editor-fold>
                                    
                                    //<editor-fold defaultstate="collapsed" desc="### CLOSE Sheet 3">
                                    /*SXSSFSheet sheet03 = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_MONTH_LACK, loginLanguage));
                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                        sheet03.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                        sheet03.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
                                        sheet03.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                        sheet03.setColumnWidth(3, 18 * 255);
                                        sheet03.setColumnWidth(4, 24 * 255);
                                        sheet03.setColumnWidth(5, 28 * 255);
                                        sheet03.setColumnWidth(6, 24 * 255);
                                        sheet03.setColumnWidth(7, 24 * 255);
                                        sheet03.setColumnWidth(8, 24 * 255);
                                        sheet03.setColumnWidth(9, 24 * 255);
                                        sheet03.setColumnWidth(10, 24 * 255);
                                        sheet03.setColumnWidth(11, 24 * 255);
                                        sheet03.setColumnWidth(12, 24 * 255);
                                        sheet03.setColumnWidth(13, 24 * 255);
                                        sheet03.setColumnWidth(14, 18 * 255);
                                        sheet03.setColumnWidth(15, 18 * 255);
                                        sheet03.setColumnWidth(16, 18 * 255);
                                        sheet03.setColumnWidth(17, 18 * 255);
                                        sheet03.setColumnWidth(18, 18 * 255);
                                        sheet03.setColumnWidth(19, 18 * 255);
                                        sheet03.setColumnWidth(20, 28 * 255);
                                        sheet03.setColumnWidth(21, 28 * 255);
                                        sheet03.setColumnWidth(22, 28 * 255);
                                        sheet03.setColumnWidth(23, 28 * 255);
                                        sheet03.setColumnWidth(24, 28 * 255);
                                        sheet03.setColumnWidth(25, 28 * 255);
                                        sheet03.setColumnWidth(26, 28 * 255);
                                        sheet03.setColumnWidth(27, 28 * 255);
                                        sheet03.setColumnWidth(28, 28 * 255);
                                        sheet03.setColumnWidth(29, 28 * 255);
                                        sheet03.setColumnWidth(30, 28 * 255);
                                        sheet03.setColumnWidth(31, 28 * 255);
                                        sheet03.setColumnWidth(32, 28 * 255);
                                        sheet03.setColumnWidth(33, 28 * 255);
                                        sheet03.setColumnWidth(34, 28 * 255);
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

                                        Cell cell113 = row3.createCell((short) 3);
                                        cell113.setCellValue(sCellMonthControl);
                                        cell113.setCellStyle(my_style3);

                                        Cell cell333 = row3.createCell((short) 4);
                                        cell333.setCellValue(sCellMST);
                                        cell333.setCellStyle(my_style3);

                                        Cell cell53 = row3.createCell((short) 5);
                                        cell53.setCellValue(sCellCompany);
                                        cell53.setCellStyle(my_style3);

                                        Cell cell93 = row3.createCell((short) 6);
                                        cell93.setCellValue(sCellProfile);
                                        cell93.setCellStyle(my_style3);

                                        Cell cellEffective3 = row3.createCell((short) 7);
                                        cellEffective3.setCellValue(sCelCertEffective);
                                        cellEffective3.setCellStyle(my_style);

                                        Cell cellExpire3 = row3.createCell((short) 8);
                                        cellExpire3.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EXPIRATION_CONTRACT, loginLanguage));
                                        cellExpire3.setCellStyle(my_style);

                                        Cell cell1924 = row3.createCell((short) 9);
                                        cell1924.setCellValue(sCellCertSN);
                                        cell1924.setCellStyle(my_style3);

                                        Cell cell103 = row3.createCell((short) 10);
                                        cell103.setCellValue(sCellRequestType);
                                        cell103.setCellStyle(my_style3);

                                        Cell cell1032 = row3.createCell((short) 11);
                                        cell1032.setCellValue(sCellStateCert);
                                        cell1032.setCellStyle(my_style3);

                                        Cell cell123 = row3.createCell((short) 12);
                                        cell123.setCellValue(sCellStateProfile);
                                        cell123.setCellStyle(my_style3);

                                        Cell cellStatusLegal = row3.createCell((short) 13);
                                        cellStatusLegal.setCellValue(sCellProfileLegalStatus);
                                        cellStatusLegal.setCellStyle(my_style3);
                                        
                                        Cell cellCheckRegister = row3.createCell((short) 14);
                                        cellCheckRegister.setCellValue(sCelCheckRegister);
                                        cellCheckRegister.setCellStyle(my_style3);
                                        
                                        Cell cellCheckConfirm = row3.createCell((short) 15);
                                        cellCheckConfirm.setCellValue(sCelCheckConfirm);
                                        cellCheckConfirm.setCellStyle(my_style3);
                                        
                                        Cell cellCheckGPKD = row3.createCell((short) 16);
                                        cellCheckGPKD.setCellValue(sCelCheckGPKD);
                                        cellCheckGPKD.setCellStyle(my_style3);
                                        
                                        Cell cellCheckStatusBusiness = row3.createCell((short) 17);
                                        cellCheckStatusBusiness.setCellValue(sCelCheckStatusBusiness);
                                        cellCheckStatusBusiness.setCellStyle(my_style3);
                                        
                                        Cell cellCheckCMND = row3.createCell((short) 18);
                                        cellCheckCMND.setCellValue(sCelCheckCMND);
                                        cellCheckCMND.setCellStyle(my_style3);
                                        
                                        Cell cellCheckStatusCMND = row3.createCell((short) 19);
                                        cellCheckStatusCMND.setCellValue(sCelCheckStatusCMND);
                                        cellCheckStatusCMND.setCellStyle(my_style3);
                                        
                                        Cell cellAddress = row3.createCell((short) 20);
                                        cellAddress.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_ADDRESS, loginLanguage));
                                        cellAddress.setCellStyle(my_style3);
                                        
                                        Cell cellRepresentative = row3.createCell((short) 21);
                                        cellRepresentative.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_NAME, loginLanguage));
                                        cellRepresentative.setCellStyle(my_style3);
                                        
                                        Cell cellPosition = row3.createCell((short) 22);
                                        cellPosition.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_POSITION, loginLanguage));
                                        cellPosition.setCellStyle(my_style3);
                                        
                                        Cell cellPhoneRepres = row3.createCell((short) 23);
                                        cellPhoneRepres.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_PHONE, loginLanguage));
                                        cellPhoneRepres.setCellStyle(my_style3);
                                        
                                        Cell cellEmailRepres = row3.createCell((short) 24);
                                        cellEmailRepres.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_EMAIL, loginLanguage));
                                        cellEmailRepres.setCellStyle(my_style3);
                                        
                                        Cell cellContactName = row3.createCell((short) 25);
                                        cellContactName.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CONTACT_NAME, loginLanguage));
                                        cellContactName.setCellStyle(my_style3);

                                        Cell cell63 = row3.createCell((short) 26);
                                        cell63.setCellValue(sCellCMND);
                                        cell63.setCellStyle(my_style3);

                                        Cell cell73 = row3.createCell((short) 27);
                                        cell73.setCellValue(sCellPesonal);
                                        cell73.setCellStyle(my_style3);

                                        Cell cell1931 = row3.createCell((short) 28);
                                        cell1931.setCellValue(sCellDateReceive);
                                        cell1931.setCellStyle(my_style3);

                                        Cell cell153 = row3.createCell((short) 29);
                                        cell153.setCellValue(sCellControlState);
                                        cell153.setCellStyle(my_style3);

                                        Cell cell193 = row3.createCell((short) 30);
                                        cell193.setCellValue(sCellAmountFine);
                                        cell193.setCellStyle(my_style3);

                                        Cell cellS1 = row3.createCell((short) 31);
                                        cellS1.setCellValue(sCelTokenState);
                                        cellS1.setCellStyle(my_style3);

                                        Cell cell1921 = row3.createCell((short) 32);
                                        cell1921.setCellValue(sCellTokenSN);
                                        cell1921.setCellStyle(my_style3);

                                        Cell cellS2 = row3.createCell((short) 33);
                                        cellS2.setCellValue(sCelTokenWaitLock);
                                        cellS2.setCellStyle(my_style3);

                                        Cell cellS3 = row3.createCell((short) 34);
                                        cellS3.setCellValue(sCelTokenWaitUnlock);
                                        cellS3.setCellStyle(my_style3);

                                        int ii = 1;
                                        int kk = 0;
                                        for (CERTIFICATION rsReportBranch1 : rsReportBranch[0]) {
                                            if(rsReportBranch1.COLLECT_ENABLED == false) {
                                                if (kk == 0) {
                                                    kk = 1;
                                                } else {
                                                    kk++;
                                                }
                                                Row row11 = sheet03.createRow(ii);
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                my_styleBranch.setFont(fontBranch);

                                                CellStyle my_styleBranchDate = wb.createCellStyle();
                                                Font fontBranchDate = wb.createFont();
                                                fontBranchDate.setFontName("Arial");
                                                my_styleBranchDate.setFont(fontBranchDate);
                                                my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));

                                                Cell cellBranch = row11.createCell((short) 0);
                                                cellBranch.setCellValue(kk);
                                                cellBranch.setCellStyle(my_styleBranch);

                                                Cell cellBranch1 = row11.createCell((short) 1);
                                                cellBranch1.setCellValue(rsReportBranch1.BRANCH_DESC);
                                                cellBranch1.setCellStyle(my_styleBranch);

                                                Cell cellBranch2 = row11.createCell((short) 2);
                                                cellBranch2.setCellValue(rsReportBranch1.CREATED_BY);
                                                cellBranch2.setCellStyle(my_styleBranch);

                                                Cell cellBranch11 = row11.createCell((short) 3);
                                                cellBranch11.setCellValue(rsReportBranch1.CROSS_CHECKED_MOUNTH);
                                                cellBranch11.setCellStyle(my_styleBranch);

                                                String sMSTAndBUDGET_CODE = CommonReferServlet.filterPrefixUIDLanguage(rsReportBranch1.ENTERPRISE_ID, loginLanguage);
                                                Cell cellBranch3 = row11.createCell((short) 4);
                                                cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                                cellBranch3.setCellStyle(styleText);

                                                Cell cellBranch5 = row11.createCell((short) 5);
                                                cellBranch5.setCellValue(rsReportBranch1.COMPANY_NAME);
                                                cellBranch5.setCellStyle(my_styleBranch);

                                                Cell cellBranch9 = row11.createCell((short) 6);
                                                cellBranch9.setCellValue(rsReportBranch1.CERTIFICATION_PROFILE_NAME);
                                                cellBranch9.setCellStyle(my_styleBranch);

                                                Cell valueEffective = row11.createCell((short) 7);
                                                Date dateEffective = CommonFunction.convertStringToDate(rsReportBranch1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateEffective != null) {
                                                    valueEffective.setCellValue(dateEffective);
                                                } else {
                                                    valueEffective.setCellValue("");
                                                }
                                                valueEffective.setCellStyle(my_styleBranchDate);

                                                Cell valueExpire = row11.createCell((short) 8);
                                                Date dateExpire = CommonFunction.convertStringToDate(rsReportBranch1.EXPIRATION_CONTRACT_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateExpire != null) {
                                                    valueExpire.setCellValue(dateExpire);
                                                } else {
                                                    valueExpire.setCellValue("");
                                                }
                                                valueExpire.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch1414 = row11.createCell((short) 9);
                                                cellBranch1414.setCellValue(rsReportBranch1.CERTIFICATION_SN);
                                                cellBranch1414.setCellStyle(styleText);
                                                
                                                Cell cellBranch10 = row11.createCell((short) 10);
                                                cellBranch10.setCellValue(rsReportBranch1.CERTIFICATION_ATTR_TYPE_DESC);
                                                cellBranch10.setCellStyle(my_styleBranch);

                                                Cell cellBranch101 = row11.createCell((short) 11);
                                                cellBranch101.setCellValue(rsReportBranch1.CERTIFICATION_STATE_DESC);
                                                cellBranch101.setCellStyle(my_styleBranch);

                                                Cell cellBranch12 = row11.createCell((short) 12);
                                                cellBranch12.setCellValue(rsReportBranch1.FILE_MANAGER_STATE_DESC);
                                                cellBranch12.setCellStyle(my_styleBranch);

                                                String sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_HARD_VERSION, loginLanguage);
                                                if(rsReportBranch1.COLLECT_SOFTCOPY == true) {
                                                    sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SOFT_VERSION, loginLanguage);
                                                }

                                                Cell cellBranch14123 = row11.createCell((short) 13);
                                                cellBranch14123.setCellValue(sTypeProfile);
                                                cellBranch14123.setCellStyle(my_styleBranch);

                                                String sRegister = "";
                                                String sConfirm = "";
                                                String sDKKD = "";
                                                String sCMND = "";
                                                String sStatusDKKD = "";
                                                String sStatusCMND = "";
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
                                                        boolean bStatusDKKD = CommonFunction.checkBriefFileScan(Definitions.CONFIG_FILE_PROFILE_PHOTO_ACTIVITY_DECLARATION, resIPData);
                                                        if(bStatusDKKD == true){sStatusDKKD = "x";}
                                                        boolean bStatusCMND = CommonFunction.checkBriefFileScan(Definitions.CONFIG_FILE_PROFILE_PHOTO_ID_CARD, resIPData);
                                                        if(bStatusCMND == true){sStatusCMND = "x";}
                                                    }
                                                }
                                                Cell cellBranch21 = row11.createCell((short) 14);
                                                cellBranch21.setCellValue(sRegister);
                                                cellBranch21.setCellStyle(my_styleBranch);

                                                Cell cellBranch22 = row11.createCell((short) 15);
                                                cellBranch22.setCellValue(sConfirm);
                                                cellBranch22.setCellStyle(my_styleBranch);

                                                Cell cellBranch23 = row11.createCell((short) 16);
                                                cellBranch23.setCellValue(sDKKD);
                                                cellBranch23.setCellStyle(my_styleBranch);

                                                Cell valueStatusBusiness = row11.createCell((short) 17);
                                                valueStatusBusiness.setCellValue(sStatusDKKD);
                                                valueStatusBusiness.setCellStyle(my_styleBranch);

                                                Cell cellBranch24 = row11.createCell((short) 18);
                                                cellBranch24.setCellValue(sCMND);
                                                cellBranch24.setCellStyle(my_styleBranch);

                                                Cell valueStatusCMND = row11.createCell((short) 19);
                                                valueStatusCMND.setCellValue(sStatusCMND);
                                                valueStatusCMND.setCellStyle(my_styleBranch);
                                                //
                                                String sADDRESS = "";
                                                String sPOSITION = "";
                                                String sREPRESENTATIVE_EMAIL = "";
                                                String sREPRESENTATIVE_PHONE = "";
                                                String sREPRESENTATIVE_NAME = "";
                                                String sCONTACT_NAME = "";
                                                String sPrfileContact = EscapeUtils.CheckTextNull(rsReportBranch1.PROFILE_CONTACT_INFO);
                                                if(!"".equals(sPrfileContact)) {
                                                    ObjectMapper oMapperParse = new ObjectMapper();
                                                    ProfileContactInfoJson profileContact = oMapperParse.readValue(sPrfileContact, ProfileContactInfoJson.class);
                                                    if(profileContact != null) {
                                                        sREPRESENTATIVE_EMAIL = EscapeUtils.CheckTextNull(profileContact.RepresentativeEmail);
                                                        sREPRESENTATIVE_PHONE = EscapeUtils.CheckTextNull(profileContact.RepresentativePhone);
                                                        sREPRESENTATIVE_NAME = CommonFunction.replaceCharaterSpecialJson(profileContact.RepresentativeName, false);
                                                        sCONTACT_NAME = CommonFunction.replaceCharaterSpecialJson(profileContact.ContactName, false);
                                                        sADDRESS = CommonFunction.replaceCharaterSpecialJson(profileContact.Address, false);
                                                        sPOSITION = CommonFunction.replaceCharaterSpecialJson(profileContact.Position, false);
                                                    }
                                                }
                                                Cell cellADDRESS = row11.createCell((short) 20);
                                                cellADDRESS.setCellValue(sADDRESS);
                                                cellADDRESS.setCellStyle(my_styleBranch);
                                                
                                                Cell cellREPRESENTATIVE_NAME = row11.createCell((short) 21);
                                                cellREPRESENTATIVE_NAME.setCellValue(sREPRESENTATIVE_NAME);
                                                cellREPRESENTATIVE_NAME.setCellStyle(my_styleBranch);
                                                
                                                Cell cellPOSITION = row11.createCell((short) 22);
                                                cellPOSITION.setCellValue(sPOSITION);
                                                cellPOSITION.setCellStyle(my_styleBranch);
                                                
                                                Cell cellREPRESENTATIVE_PHONE = row11.createCell((short) 23);
                                                cellREPRESENTATIVE_PHONE.setCellValue(sREPRESENTATIVE_PHONE);
                                                cellREPRESENTATIVE_PHONE.setCellStyle(my_styleBranch);
                                                
                                                Cell cellREPRESENTATIVE_EMAIL = row11.createCell((short) 24);
                                                cellREPRESENTATIVE_EMAIL.setCellValue(sREPRESENTATIVE_EMAIL);
                                                cellREPRESENTATIVE_EMAIL.setCellStyle(my_styleBranch);
                                                
                                                Cell cellCONTACT_NAME = row11.createCell((short) 25);
                                                cellCONTACT_NAME.setCellValue(sCONTACT_NAME);
                                                cellCONTACT_NAME.setCellStyle(my_styleBranch);
                                                //
                                                String sP_IDAndPASSPORT = CommonReferServlet.filterPrefixUIDLanguage(rsReportBranch1.PERSONAL_ID, loginLanguage);
                                                Cell cellBranch6 = row11.createCell((short) 26);
                                                cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                                cellBranch6.setCellStyle(styleText);

                                                Cell cellBranch7 = row11.createCell((short) 27);
                                                cellBranch7.setCellValue(rsReportBranch1.PERSONAL_NAME);
                                                cellBranch7.setCellStyle(my_styleBranch);

                                                Cell cellBranch1512 = row11.createCell((short) 28);
                                                Date dateReceive = CommonFunction.convertStringToDate(rsReportBranch1.RECEIVED_BRIEF_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateReceive != null) {
                                                    cellBranch1512.setCellValue(dateReceive);
                                                } else {
                                                    cellBranch1512.setCellValue("");
                                                }
                                                cellBranch1512.setCellStyle(my_styleBranchDate);
                                                
                                                String sCOLLECT_ENABLED = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_CHECKED, loginLanguage);
                                                if(rsReportBranch1.COLLECT_ENABLED == false) {
                                                    sCOLLECT_ENABLED = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_NOT_CONTROL, loginLanguage);
                                                }
                                                Cell cellBranch15222 = row11.createCell((short) 29);
                                                cellBranch15222.setCellValue(sCOLLECT_ENABLED);
                                                cellBranch15222.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch15 = row11.createCell((short) 30);
                                                cellBranch15.setCellValue(rsReportBranch1.FINE_FOR_LACK_OF_BRIEF);
                                                cellBranch15.setCellStyle(my_styleBranch);

                                                Cell cellBranch27 = row11.createCell((short) 31);
                                                cellBranch27.setCellValue(rsReportBranch1.TOKEN_STATE_DESC);
                                                cellBranch27.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch1412 = row11.createCell((short) 32);
                                                cellBranch1412.setCellValue(rsReportBranch1.TOKEN_SN);
                                                cellBranch1412.setCellStyle(styleText);
                                                
                                                Cell cellBranch28 = row11.createCell((short) 33);
                                                cellBranch28.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_LOCK) ? "x" : "");
                                                cellBranch28.setCellStyle(my_styleBranch);

                                                Cell cellBranch29 = row11.createCell((short) 34);
                                                cellBranch29.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_UNLOCK) ? "x" : "");
                                                cellBranch29.setCellStyle(my_styleBranch);
                                                ii++;
                                            }
                                        }
                                    } else {
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
                                        sheet03.setColumnWidth(22, 18 * 255);
                                        sheet03.setColumnWidth(23, 18 * 255);
                                        sheet03.setColumnWidth(24, 18 * 255);
                                        sheet03.setColumnWidth(25, 24 * 255);
                                        sheet03.setColumnWidth(26, 24 * 255);
                                        sheet03.setColumnWidth(27, 24 * 255);
                                        sheet03.setColumnWidth(28, 24 * 255);
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
                                        cell143.setCellValue(sCellProfileLegalStatus);
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

                                        Cell cellStatusBusiness1 = row3.createCell((short) 22);
                                        cellStatusBusiness1.setCellValue(sCelCheckStatusBusiness);
                                        cellStatusBusiness1.setCellStyle(my_style);

                                        Cell cellStatusCMND1 = row3.createCell((short) 23);
                                        cellStatusCMND1.setCellValue(sCelCheckStatusCMND);
                                        cellStatusCMND1.setCellStyle(my_style);

                                        Cell cellS1 = row3.createCell((short) 24);
                                        cellS1.setCellValue(sCelTokenState);
                                        cellS1.setCellStyle(my_style3);

                                        Cell cellS2 = row3.createCell((short) 25);
                                        cellS2.setCellValue(sCelTokenWaitLock);
                                        cellS2.setCellStyle(my_style3);

                                        Cell cellS3 = row3.createCell((short) 26);
                                        cellS3.setCellValue(sCelTokenWaitUnlock);
                                        cellS3.setCellStyle(my_style3);

                                        Cell cellEffective3 = row3.createCell((short) 27);
                                        cellEffective3.setCellValue(sCelCertEffective);
                                        cellEffective3.setCellStyle(my_style);

                                        Cell cellExpire3 = row3.createCell((short) 28);
                                        cellExpire3.setCellValue(sCelCertExpire);
                                        cellExpire3.setCellStyle(my_style);

                                        int ii = 1;
                                        int kk = 0;
                                        for (CERTIFICATION rsReportBranch1 : rsReportBranch[0]) {
                                            if(rsReportBranch1.COLLECT_ENABLED == false) {
                                                if (kk == 0) {
                                                    kk = 1;
                                                } else {
                                                    kk++;
                                                }
                                                Row row11 = sheet03.createRow(ii);
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                my_styleBranch.setFont(fontBranch);

                                                CellStyle my_styleBranchDate = wb.createCellStyle();
                                                Font fontBranchDate = wb.createFont();
                                                fontBranchDate.setFontName("Arial");
                                                my_styleBranchDate.setFont(fontBranchDate);
                                                my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));

                                                Cell cellBranch = row11.createCell((short) 0);
                                                cellBranch.setCellValue(kk);
                                                cellBranch.setCellStyle(my_styleBranch);

                                                Cell cellBranch1 = row11.createCell((short) 1);
                                                cellBranch1.setCellValue(rsReportBranch1.BRANCH_DESC);
                                                cellBranch1.setCellStyle(my_styleBranch);

                                                Cell cellBranch2 = row11.createCell((short) 2);
                                                cellBranch2.setCellValue(rsReportBranch1.CREATED_BY);
                                                cellBranch2.setCellStyle(my_styleBranch);

                                                String sMSTAndBUDGET_CODE = rsReportBranch1.ENTERPRISE_ID;
                                                Cell cellBranch3 = row11.createCell((short) 3);
                                                cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                                cellBranch3.setCellStyle(styleText);

                                                Cell cellBranch5 = row11.createCell((short) 4);
                                                cellBranch5.setCellValue(rsReportBranch1.COMPANY_NAME);
                                                cellBranch5.setCellStyle(my_styleBranch);

                                                String sP_IDAndPASSPORT = rsReportBranch1.PERSONAL_ID;
                                                Cell cellBranch6 = row11.createCell((short) 5);
                                                cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                                cellBranch6.setCellStyle(styleText);

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

                                                String sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_HARD_VERSION, loginLanguage);
                                                if(rsReportBranch1.COLLECT_SOFTCOPY == true) {
                                                    sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SOFT_VERSION, loginLanguage);
                                                }

                                                Cell cellBranch14123 = row11.createCell((short) 12);
                                                cellBranch14123.setCellValue(sTypeProfile);
                                                cellBranch14123.setCellStyle(my_styleBranch);

                                                String sCOLLECT_ENABLED = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_CHECKED, loginLanguage);
                                                if(rsReportBranch1.COLLECT_ENABLED == false) {
                                                    sCOLLECT_ENABLED = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_NOT_CONTROL, loginLanguage);
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
                                                cellBranch1414.setCellStyle(styleText);

                                                Cell cellBranch1412 = row11.createCell((short) 17);
                                                cellBranch1412.setCellValue(rsReportBranch1.TOKEN_SN);
                                                cellBranch1412.setCellStyle(styleText);

                                                String sRegister = "";
                                                String sConfirm = "";
                                                String sDKKD = "";
                                                String sCMND = "";
                                                String sStatusDKKD = "";
                                                String sStatusCMND = "";
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
                                                        boolean bStatusDKKD = CommonFunction.checkBriefFileScan(Definitions.CONFIG_FILE_PROFILE_PHOTO_ACTIVITY_DECLARATION, resIPData);
                                                        if(bStatusDKKD == true){sStatusDKKD = "x";}
                                                        boolean bStatusCMND = CommonFunction.checkBriefFileScan(Definitions.CONFIG_FILE_PROFILE_PHOTO_ID_CARD, resIPData);
                                                        if(bStatusCMND == true){sStatusCMND = "x";}
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

                                                Cell valueStatusBusiness = row11.createCell((short) 22);
                                                valueStatusBusiness.setCellValue(sStatusDKKD);
                                                valueStatusBusiness.setCellStyle(my_styleBranch);

                                                Cell valueStatusCMND = row11.createCell((short) 23);
                                                valueStatusCMND.setCellValue(sStatusCMND);
                                                valueStatusCMND.setCellStyle(my_styleBranch);

                                                Cell cellBranch27 = row11.createCell((short) 24);
                                                cellBranch27.setCellValue(rsReportBranch1.TOKEN_STATE_DESC);
                                                cellBranch27.setCellStyle(my_styleBranch);

                                                Cell cellBranch28 = row11.createCell((short) 25);
                                                cellBranch28.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_LOCK) ? "x" : "");
                                                cellBranch28.setCellStyle(my_styleBranch);

                                                Cell cellBranch29 = row11.createCell((short) 26);
                                                cellBranch29.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_UNLOCK) ? "x" : "");
                                                cellBranch29.setCellStyle(my_styleBranch);

                                                Cell valueEffective = row11.createCell((short) 27);
                                                Date dateEffective = CommonFunction.convertStringToDate(rsReportBranch1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateEffective != null) {
                                                    valueEffective.setCellValue(dateEffective);
                                                } else {
                                                    valueEffective.setCellValue("");
                                                }
                                                valueEffective.setCellStyle(my_styleBranchDate);

                                                Cell valueExpire = row11.createCell((short) 28);
                                                Date dateExpire = CommonFunction.convertStringToDate(rsReportBranch1.EXPIRATION_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateExpire != null) {
                                                    valueExpire.setCellValue(dateExpire);
                                                } else {
                                                    valueExpire.setCellValue("");
                                                }
                                                valueExpire.setCellStyle(my_styleBranchDate);
                                                ii++;
                                            }
                                        }
                                    }*/
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
                            case "exportcollationcertlist_20": {
                                //<editor-fold defaultstate="collapsed" desc="exportcollationcertlist_20">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
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
                                    CommonFunction.LogDebugString(logServlet, "Export Collation Data", "Mounth: " + FromCreateDate + "; Year: " + ToCreateDate + "; STATUS_COLLATION: " + STATUS_COLLATION);
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
                                            
                                            CellStyle my_styleBranch = wb.createCellStyle();
                                            my_styleBranch.setFont(fontBranch);
                                            CellStyle my_styleBranchBold = wb.createCellStyle();
                                            my_styleBranchBold.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_MONEY_EXCEL));
                                            Font fontBranchBold = wb.createFont();
                                            fontBranchBold.setBoldweight((short) 700);
                                            fontBranchBold.setFontName("Verdana");
                                            my_styleBranchBold.setFont(fontBranchBold);
                                            
                                            CellStyle my_styleMoney = wb.createCellStyle();
                                            my_styleMoney.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_MONEY_EXCEL));
                                            my_styleMoney.setFont(fontBranch);
                                            my_styleMoney.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                                            my_styleMoney.setBorderTop(HSSFCellStyle.BORDER_THIN);
                                            my_styleMoney.setBorderRight(HSSFCellStyle.BORDER_THIN);
                                            my_styleMoney.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                                            int sumNumToken = 0;
                                            int sumRoseAmount = 0;
                                            int sumTokenAmout = 0;
                                            int sumReturnAmount = 0;
                                            
                                            //<editor-fold defaultstate="collapsed" desc="### CLOSE TONG HOP NEW">
                                            /*SXSSFSheet sheet = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CUSTOMER_DETAIL, loginLanguage).trim());
                                            
                                            BRANCH[][] rsBranch = new BRANCH[1][];
                                            String sBranchReport = "";
                                            com.S_BO_BRANCH_DETAIL(EscapeUtils.escapeHtmlSearch(idBranchOffice), rsBranch);
                                            if(rsBranch[0].length > 0) {
                                                sBranchReport = EscapeUtils.CheckTextNull(rsBranch[0][0].REMARK);
                                            }*/
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - HEADER">
                                            /*i = 0;
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
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);*/
                                            //</editor-fold>
                                            
                                            //i = i + 2;
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET LIST">
                                            /*int sumNumToken = 0;
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
                                                celTokenAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_FEE, loginLanguage));
                                                celTokenAmount.setCellStyle(my_style);
                                                Cell celRoseAmount = rowCusDetail.createCell((short) 9);
                                                celRoseAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_ROSE, loginLanguage));
                                                celRoseAmount.setCellStyle(my_style);
                                                Cell celSumAmount = rowCusDetail.createCell((short) 10);
                                                celSumAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_MONEY_CHARGES_CA, loginLanguage));
                                                celSumAmount.setCellStyle(my_style);
                                                Cell celDateEffective = rowCusDetail.createCell((short) 11);
                                                celDateEffective.setCellValue(sCellDateEffective);
                                                celDateEffective.setCellStyle(my_style);
                                                Cell celDateRevoke = rowCusDetail.createCell((short) 12);
                                                celDateRevoke.setCellValue(sExpirationContractDT);
                                                celDateRevoke.setCellStyle(my_style);
                                                Cell celCertSN = rowCusDetail.createCell((short) 13);
                                                celCertSN.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage));
                                                celCertSN.setCellStyle(my_style);
                                                Cell celRequestType = rowCusDetail.createCell((short) 14);
                                                celRequestType.setCellValue(sCellRequestType);
                                                celRequestType.setCellStyle(my_style);
                                                Cell celCreateUser = rowCusDetail.createCell((short) 15);
                                                celCreateUser.setCellValue(sCellUser);
                                                celCreateUser.setCellStyle(my_style);
                                                Cell celCus6 = rowCusDetail.createCell((short) 16);
                                                celCus6.setCellValue(sCellCMND);
                                                celCus6.setCellStyle(my_style);
                                                Cell celCus7 = rowCusDetail.createCell((short) 17);
                                                celCus7.setCellValue(sCellPersonal);
                                                celCus7.setCellStyle(my_style);
                                                Cell celTokenSN = rowCusDetail.createCell((short) 18);
                                                celTokenSN.setCellValue(sTokenSN);
                                                celTokenSN.setCellStyle(my_style);
                                                Cell celPastCertType = rowCusDetail.createCell((short) 19);
                                                celPastCertType.setCellValue(sPastCertType);
                                                celPastCertType.setCellStyle(my_style);
                                                Cell celPastCertEffective = rowCusDetail.createCell((short) 20);
                                                celPastCertEffective.setCellValue(sPastCertEffective);
                                                celPastCertEffective.setCellStyle(my_style);
                                                Cell celPastProfileStatus = rowCusDetail.createCell((short) 21);
                                                celPastProfileStatus.setCellValue(sPastProfileStatus);
                                                celPastProfileStatus.setCellStyle(my_style);
                                                Cell celPastAgentCode = rowCusDetail.createCell((short) 22);
                                                celPastAgentCode.setCellValue(sPastAgentCode);
                                                celPastAgentCode.setCellStyle(my_style);
                                                
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

                                                    Cell cellBranch1 = row1.createCell((short) 2);
                                                    cellBranch1.setCellValue(temp1.BRANCH_NAME);
                                                    cellBranch1.setCellStyle(my_styleBranch);

                                                    String sTAX_CODE = temp1.ENTERPRISE_ID;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                        sTAX_CODE = CommonReferServlet.filterPrefixUIDLanguage(temp1.ENTERPRISE_ID, loginLanguage);
                                                    }
                                                    Cell cellBranch4 = row1.createCell((short) 3);
                                                    cellBranch4.setCellValue(sTAX_CODE);
                                                    cellBranch4.setCellStyle(styleText);

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
                                                    cellBranch11.setCellStyle(my_styleBranch);

                                                    Cell cellBranch12 = row1.createCell((short) 8);
                                                    cellBranch12.setCellValue(temp1.TOKEN_AMOUNT);
                                                    cellBranch12.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellRoseAmount = row1.createCell((short) 9);
                                                    cellRoseAmount.setCellValue(temp1.ROSE_AMOUNT);
                                                    cellRoseAmount.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellSumAmount = row1.createCell((short) 10);
                                                    cellSumAmount.setCellValue(temp1.RETURN_AMOUNT);
                                                    cellSumAmount.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellValueEffective = row1.createCell((short) 11);
                                                    Date dateEffective = CommonFunction.convertStringToDate(temp1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateEffective != null) {
                                                        cellValueEffective.setCellValue(dateEffective);
                                                    } else {
                                                        cellValueEffective.setCellValue("");
                                                    }
                                                    cellValueEffective.setCellStyle(my_styleBranchDate);
                                                    
                                                    Cell cellValueRevoke = row1.createCell((short) 12);
                                                    Date dateRevoke = CommonFunction.convertStringToDate(temp1.EXPIRATION_CONTRACT_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateRevoke != null) {
                                                        cellValueRevoke.setCellValue(dateRevoke);
                                                    } else {
                                                        cellValueRevoke.setCellValue("");
                                                    }
                                                    cellValueRevoke.setCellStyle(my_styleBranchDate);
                                                    
                                                    String sCERTIFICATION_SN = temp1.CERTIFICATION_SN;
                                                    Cell cellBranch19 = row1.createCell((short) 13);
                                                    cellBranch19.setCellValue(sCERTIFICATION_SN);
                                                    cellBranch19.setCellStyle(styleText);

                                                    Cell cellBranch3 = row1.createCell((short) 14);
                                                    cellBranch3.setCellValue(temp1.SERVICE_TYPE_DESC);
                                                    cellBranch3.setCellStyle(my_styleBranch);

                                                    Cell cellBranch2 = row1.createCell((short) 15);
                                                    cellBranch2.setCellValue(temp1.CREATED_BY);
                                                    cellBranch2.setCellStyle(my_styleBranch);

                                                    String sP_ID = temp1.PERSONAL_ID;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                        sP_ID = CommonReferServlet.filterPrefixUIDLanguage(temp1.PERSONAL_ID, loginLanguage);
                                                    }
                                                    Cell cellBranch6 = row1.createCell((short) 16);
                                                    cellBranch6.setCellValue(sP_ID);
                                                    cellBranch6.setCellStyle(styleText);

                                                    Cell cellBranch7 = row1.createCell((short) 17);
                                                    cellBranch7.setCellValue(temp1.PERSONAL_NAME);
                                                    cellBranch7.setCellStyle(my_styleBranch);
                                                    
                                                    String valueTokenSN = temp1.TOKEN_SN;
                                                    if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                    Cell cellBranch21 = row1.createCell((short) 18);
                                                    cellBranch21.setCellValue(valueTokenSN);
                                                    cellBranch21.setCellStyle(styleText);
                                                    
                                                    Cell valuePastCertType = row1.createCell((short) 19);
                                                    valuePastCertType.setCellValue(temp1.PAST_SERVICE_TYPE_DESC);
                                                    valuePastCertType.setCellStyle(my_styleBranch);
                                                    
                                                    Cell valuePastCertEffective = row1.createCell((short) 20);
                                                    Date datePastEffective = CommonFunction.convertStringToDate(temp1.PAST_EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(datePastEffective != null) {
                                                        valuePastCertEffective.setCellValue(datePastEffective);
                                                    } else {
                                                        valuePastCertEffective.setCellValue("");
                                                    }
                                                    valuePastCertEffective.setCellStyle(my_styleBranchDate);
                                                    
                                                    Cell valuePastProfileStatus = row1.createCell((short) 21);
                                                    valuePastProfileStatus.setCellValue(temp1.PAST_FILE_MANAGER_STATE_DESC);
                                                    valuePastProfileStatus.setCellStyle(my_styleBranch);
                                                    
                                                    Cell valuePastBranchDesc = row1.createCell((short) 22);
                                                    valuePastBranchDesc.setCellValue(temp1.PAST_BRANCH_DESC);
                                                    valuePastBranchDesc.setCellStyle(my_styleBranch);
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
                                            }*/
                                            //</editor-fold>
                                            
                                            //i = i + 2;
                                            //int iSum = i;
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - TOKEN">
                                            /*CellStyle my_styleBranch = wb.createCellStyle();
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
                                            Row row17 = sheet.createRow(i);*/
                                            //</editor-fold>
                                            
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - SUM">
                                            /*int iSumAllMoneyStart = 0;
                                            int iSumAllTokenStart = 0;
                                            int iSumBonus = 0;
                                            int iSumBeforeVAT = 0;
                                            int iSumVAT = 0;
                                            int iSumAfterVAT = 0;
                                            iSumAllMoneyStart = iSum+1;
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
                                            cellProfile05.setCellValue(sumReturnAmount);
                                            cellProfile05.setCellStyle(my_styleMoney);
                                            iSum = iSum + 1;
                                            iSumAllTokenStart = iSum+1;
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
                                            iSum = iSum + 1;
                                            iSumBonus = iSum+1;
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
                                            iSum = iSum + 1;
                                            iSumBeforeVAT = iSum+1;
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
                                            iSum = iSum + 1;
                                            iSumVAT = iSum+1;
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
                                            iSum = iSum + 1;
                                            iSumAfterVAT = iSum+1;
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
                                            iSum = iSum + 1;
                                            int iSumCustody = iSum+1;
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
                                            iSum = iSum + 1;
                                            int iSumNotPaid = iSum+1;
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
                                            iSum = iSum + 1;
                                            int iSumAllMoney = iSum+1;
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
                                            iSum = iSum + 1;
                                            int iSumAdvance = iSum+1;
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
                                            iSum = iSum + 1;
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
                                            cellProfile135.setCellStyle(my_styleMoney);*/
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
                                                Row rowCusDetail = sheetAll.createRow(0);
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
                                                celTokenAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_FEE, loginLanguage));
                                                celTokenAmount.setCellStyle(my_style);
                                                Cell celRoseAmount = rowCusDetail.createCell((short) 9);
                                                celRoseAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_ROSE, loginLanguage));
                                                celRoseAmount.setCellStyle(my_style);
                                                Cell celSumAmount = rowCusDetail.createCell((short) 10);
                                                celSumAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_MONEY_CHARGES_CA, loginLanguage));
                                                celSumAmount.setCellStyle(my_style);
                                                Cell celDateEffective = rowCusDetail.createCell((short) 11);
                                                celDateEffective.setCellValue(sCellDateEffective);
                                                celDateEffective.setCellStyle(my_style);
                                                Cell celDateRevoke = rowCusDetail.createCell((short) 12);
                                                celDateRevoke.setCellValue(sExpirationContractDT);
                                                celDateRevoke.setCellStyle(my_style);
                                                Cell celCertSN = rowCusDetail.createCell((short) 13);
                                                celCertSN.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage));
                                                celCertSN.setCellStyle(my_style);
                                                Cell celRequestType = rowCusDetail.createCell((short) 14);
                                                celRequestType.setCellValue(sCellRequestType);
                                                celRequestType.setCellStyle(my_style);
                                                Cell celCreateUser = rowCusDetail.createCell((short) 15);
                                                celCreateUser.setCellValue(sCellUser);
                                                celCreateUser.setCellStyle(my_style);
                                                Cell celCus6 = rowCusDetail.createCell((short) 16);
                                                celCus6.setCellValue(sCellCMND);
                                                celCus6.setCellStyle(my_style);
                                                Cell celCus7 = rowCusDetail.createCell((short) 17);
                                                celCus7.setCellValue(sCellPersonal);
                                                celCus7.setCellStyle(my_style);
                                                Cell celTokenSN = rowCusDetail.createCell((short) 18);
                                                celTokenSN.setCellValue(sTokenSN);
                                                celTokenSN.setCellStyle(my_style);
                                                Cell celPastCertType = rowCusDetail.createCell((short) 19);
                                                celPastCertType.setCellValue(sPastCertType);
                                                celPastCertType.setCellStyle(my_style);
                                                Cell celPastCertEffective = rowCusDetail.createCell((short) 20);
                                                celPastCertEffective.setCellValue(sPastCertEffective);
                                                celPastCertEffective.setCellStyle(my_style);
                                                
                                                Cell celPastProfileStatus = rowCusDetail.createCell((short) 21);
                                                celPastProfileStatus.setCellValue(sPastProfileStatus);
                                                celPastProfileStatus.setCellStyle(my_style);
                                                
                                                Cell celPastAgentCode = rowCusDetail.createCell((short) 22);
                                                celPastAgentCode.setCellValue(sPastAgentCode);
                                                celPastAgentCode.setCellStyle(my_style);
                                                
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

                                                    Cell cellBranch1 = rowAll.createCell((short) 2);
                                                    cellBranch1.setCellValue(temp1.BRANCH_NAME);
                                                    cellBranch1.setCellStyle(my_styleBranch);

                                                    String sTAX_CODE = temp1.ENTERPRISE_ID;
                                                    Cell cellBranch4 = rowAll.createCell((short) 3);
                                                    cellBranch4.setCellValue(sTAX_CODE);
                                                    cellBranch4.setCellStyle(styleText);

                                                    Cell cellBranch5 = rowAll.createCell((short) 4);
                                                    cellBranch5.setCellValue(temp1.COMPANY_NAME);
                                                    cellBranch5.setCellStyle(my_styleBranch);

                                                    Cell cellBranch9 = rowAll.createCell((short) 5);
                                                    cellBranch9.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
                                                    cellBranch9.setCellStyle(my_styleBranch);

                                                    Cell cellBranch10 = rowAll.createCell((short) 6);
                                                    cellBranch10.setCellValue(temp1.FEE_AMOUNT);
                                                    cellBranch10.setCellStyle(my_styleMoney);

                                                    Cell cellBranch11b = rowAll.createCell((short) 7);
                                                    cellBranch11b.setCellValue(temp1.TOKEN_NUMBER);
                                                    cellBranch11b.setCellStyle(my_styleBranch);

                                                    Cell cellBranch12b = rowAll.createCell((short) 8);
                                                    cellBranch12b.setCellValue(temp1.TOKEN_AMOUNT);
                                                    cellBranch12b.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellRoseAmount = rowAll.createCell((short) 9);
                                                    cellRoseAmount.setCellValue(temp1.ROSE_AMOUNT);
                                                    cellRoseAmount.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellSumAmount = rowAll.createCell((short) 10);
                                                    cellSumAmount.setCellValue(temp1.RETURN_AMOUNT);
                                                    cellSumAmount.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellValueEffective = rowAll.createCell((short) 11);
                                                    Date dateEffective = CommonFunction.convertStringToDate(temp1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateEffective != null) {
                                                        cellValueEffective.setCellValue(dateEffective);
                                                    } else {
                                                        cellValueEffective.setCellValue("");
                                                    }
                                                    cellValueEffective.setCellStyle(my_styleBranchDate);
                                                    
                                                    Cell cellValueRevoke = rowAll.createCell((short) 12);
                                                    Date dateRevoke = CommonFunction.convertStringToDate(temp1.EXPIRATION_CONTRACT_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateRevoke != null) {
                                                        cellValueRevoke.setCellValue(dateRevoke);
                                                    } else {
                                                        cellValueRevoke.setCellValue("");
                                                    }
                                                    cellValueRevoke.setCellStyle(my_styleBranchDate);
                                                    
                                                    String sCERTIFICATION_SN = temp1.CERTIFICATION_SN;
                                                    Cell cellBranch19 = rowAll.createCell((short) 13);
                                                    cellBranch19.setCellValue(sCERTIFICATION_SN);
                                                    cellBranch19.setCellStyle(styleText);

                                                    Cell cellBranch3 = rowAll.createCell((short) 14);
                                                    cellBranch3.setCellValue(temp1.SERVICE_TYPE_DESC);
                                                    cellBranch3.setCellStyle(my_styleBranch);

                                                    Cell cellBranch2 = rowAll.createCell((short) 15);
                                                    cellBranch2.setCellValue(temp1.CREATED_BY);
                                                    cellBranch2.setCellStyle(my_styleBranch);

                                                    String sP_ID = temp1.PERSONAL_ID;
                                                    Cell cellBranch6 = rowAll.createCell((short) 16);
                                                    cellBranch6.setCellValue(sP_ID);
                                                    cellBranch6.setCellStyle(styleText);

                                                    Cell cellBranch7 = rowAll.createCell((short) 17);
                                                    cellBranch7.setCellValue(temp1.PERSONAL_NAME);
                                                    cellBranch7.setCellStyle(my_styleBranch);
                                                    
                                                    String valueTokenSN = temp1.TOKEN_SN;
                                                    if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                    Cell cellBranch21b = rowAll.createCell((short) 18);
                                                    cellBranch21b.setCellValue(valueTokenSN);
                                                    cellBranch21b.setCellStyle(styleText);
                                                    
                                                    Cell valuePastCertType = rowAll.createCell((short) 19);
                                                    valuePastCertType.setCellValue(temp1.PAST_SERVICE_TYPE_DESC);
                                                    valuePastCertType.setCellStyle(my_styleBranch);
                                                    
                                                    Cell valuePastCertEffective = rowAll.createCell((short) 20);
                                                    Date datePastEffective = CommonFunction.convertStringToDate(temp1.PAST_EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(datePastEffective != null) {
                                                        valuePastCertEffective.setCellValue(datePastEffective);
                                                    } else {
                                                        valuePastCertEffective.setCellValue("");
                                                    }
                                                    valuePastCertEffective.setCellStyle(my_styleBranchDate);
                                                    
                                                    Cell valuePastProfileStatus = rowAll.createCell((short) 21);
                                                    valuePastProfileStatus.setCellValue(temp1.PAST_FILE_MANAGER_STATE_DESC);
                                                    valuePastProfileStatus.setCellStyle(my_styleBranch);
                                                    Cell valuePastAgentCode = rowAll.createCell((short) 22);
                                                    valuePastAgentCode.setCellValue(temp1.PAST_BRANCH_DESC);
                                                    valuePastAgentCode.setCellStyle(my_styleBranch);
                                                    i++;
                                                }
                                            }
                                            //</editor-fold>
                                            
                                            //<editor-fold defaultstate="collapsed" desc="### CLOSE SHEET Decline Customers">
                                            /*rsReportPgin = new CERTIFICATION_CONTROL_REPORT[1][];
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
                                                celTokenAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_FEE, loginLanguage));
                                                celTokenAmount.setCellStyle(my_style);
                                                Cell celRoseAmount = rowCusDetail.createCell((short) 9);
                                                celRoseAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_ROSE, loginLanguage));
                                                celRoseAmount.setCellStyle(my_style);
                                                Cell celSumAmount = rowCusDetail.createCell((short) 10);
                                                celSumAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_MONEY_CHARGES_CA, loginLanguage));
                                                celSumAmount.setCellStyle(my_style);
                                                Cell celDateGen = rowCusDetail.createCell((short) 11);
                                                celDateGen.setCellValue(sCellGenDate);
                                                celDateGen.setCellStyle(my_style);
                                                Cell celRevokeDate = rowCusDetail.createCell((short) 12);
                                                celRevokeDate.setCellValue(sCellRevokeDate);
                                                celRevokeDate.setCellStyle(my_style);
                                                Cell celCertSN = rowCusDetail.createCell((short) 13);
                                                celCertSN.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage));
                                                celCertSN.setCellStyle(my_style);
                                                Cell celRequestType = rowCusDetail.createCell((short) 14);
                                                celRequestType.setCellValue(sCellRequestType);
                                                celRequestType.setCellStyle(my_style);
                                                Cell celCreateUser = rowCusDetail.createCell((short) 15);
                                                celCreateUser.setCellValue(sCellUser);
                                                celCreateUser.setCellStyle(my_style);
                                                Cell celCus6 = rowCusDetail.createCell((short) 16);
                                                celCus6.setCellValue(sCellCMND);
                                                celCus6.setCellStyle(my_style);
                                                Cell celCus7 = rowCusDetail.createCell((short) 17);
                                                celCus7.setCellValue(sCellPersonal);
                                                celCus7.setCellStyle(my_style);
                                                Cell celTokenSN = rowCusDetail.createCell((short) 18);
                                                celTokenSN.setCellValue(sTokenSN);
                                                celTokenSN.setCellStyle(my_style);
                                                
                                                Cell celProfileStatus = rowCusDetail.createCell((short) 19);
                                                celProfileStatus.setCellValue(sPastProfileStatus);
                                                celProfileStatus.setCellStyle(my_style);
                                                
                                                Cell celAgentCode = rowCusDetail.createCell((short) 20);
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

                                                    Cell cellBranch1 = rowDecline.createCell((short) 2);
                                                    cellBranch1.setCellValue(temp1.BRANCH_NAME);
                                                    cellBranch1.setCellStyle(my_styleDecBranch);

                                                    String sTAX_CODE = temp1.ENTERPRISE_ID;
                                                    Cell cellBranch4 = rowDecline.createCell((short) 3);
                                                    cellBranch4.setCellValue(sTAX_CODE);
                                                    cellBranch4.setCellStyle(styleText);

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
                                                    
                                                    Cell cellDec22 = rowDecline.createCell((short) 12);
                                                    Date dateRevoke = CommonFunction.convertStringToDate(temp1.REVOKED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateRevoke != null) {
                                                        cellDec22.setCellValue(dateRevoke);
                                                    } else {
                                                        cellDec22.setCellValue("");
                                                    }
                                                    cellDec22.setCellStyle(my_styleBranchDate);
                                                    
                                                    String sCERTIFICATION_SN = temp1.CERTIFICATION_SN;
                                                    Cell cellBranch19 = rowDecline.createCell((short) 13);
                                                    cellBranch19.setCellValue(sCERTIFICATION_SN);
                                                    cellBranch19.setCellStyle(styleText);

                                                    Cell cellBranch3 = rowDecline.createCell((short) 14);
                                                    cellBranch3.setCellValue(temp1.SERVICE_TYPE_DESC);
                                                    cellBranch3.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch2 = rowDecline.createCell((short) 15);
                                                    cellBranch2.setCellValue(temp1.CREATED_BY);
                                                    cellBranch2.setCellStyle(my_styleDecBranch);

                                                    String sP_ID = temp1.PERSONAL_ID;
                                                    Cell cellBranch6 = rowDecline.createCell((short) 16);
                                                    cellBranch6.setCellValue(sP_ID);
                                                    cellBranch6.setCellStyle(styleText);

                                                    Cell cellBranch7 = rowDecline.createCell((short) 17);
                                                    cellBranch7.setCellValue(temp1.PERSONAL_NAME);
                                                    cellBranch7.setCellStyle(my_styleDecBranch);
                                                    
                                                    String valueTokenSN = temp1.TOKEN_SN;
                                                    if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                    Cell cellDec21 = rowDecline.createCell((short) 18);
                                                    cellDec21.setCellValue(valueTokenSN);
                                                    cellDec21.setCellStyle(styleText);
                                                    
                                                    Cell cellProfileStatus = rowDecline.createCell((short) 19);
                                                    cellProfileStatus.setCellValue(temp1.PAST_FILE_MANAGER_STATE_DESC);
                                                    cellProfileStatus.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellAgentCode = rowDecline.createCell((short) 20);
                                                    cellAgentCode.setCellValue(temp1.PAST_BRANCH_DESC);
                                                    cellAgentCode.setCellStyle(my_styleDecBranch);
                                                    i++;
                                                }
                                            }*/
                                            //</editor-fold>
                                            
                                            //<editor-fold defaultstate="collapsed" desc="### CLOSE SHEET RevokeAuto Customers">
                                            /*rsReportPgin = new CERTIFICATION_CONTROL_REPORT[1][];
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
                                                celTokenAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_FEE, loginLanguage));
                                                celTokenAmount.setCellStyle(my_style);
                                                Cell celRoseAmount = rowCusDetail.createCell((short) 9);
                                                celRoseAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_ROSE, loginLanguage));
                                                celRoseAmount.setCellStyle(my_style);
                                                Cell celSumAmount = rowCusDetail.createCell((short) 10);
                                                celSumAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_MONEY_CHARGES_CA, loginLanguage));
                                                celSumAmount.setCellStyle(my_style);
                                                Cell celDateGen = rowCusDetail.createCell((short) 11);
                                                celDateGen.setCellValue(sCellGenDate);
                                                celDateGen.setCellStyle(my_style);
                                                Cell celRevokeDate = rowCusDetail.createCell((short) 12);
                                                celRevokeDate.setCellValue(sCellRevokeDate);
                                                celRevokeDate.setCellStyle(my_style);
                                                Cell celCertSN = rowCusDetail.createCell((short) 13);
                                                celCertSN.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage));
                                                celCertSN.setCellStyle(my_style);
                                                Cell celRequestType = rowCusDetail.createCell((short) 14);
                                                celRequestType.setCellValue(sCellRequestType);
                                                celRequestType.setCellStyle(my_style);
                                                Cell celCreateUser = rowCusDetail.createCell((short) 15);
                                                celCreateUser.setCellValue(sCellUser);
                                                celCreateUser.setCellStyle(my_style);
                                                Cell celCus6 = rowCusDetail.createCell((short) 16);
                                                celCus6.setCellValue(sCellCMND);
                                                celCus6.setCellStyle(my_style);
                                                Cell celCus7 = rowCusDetail.createCell((short) 17);
                                                celCus7.setCellValue(sCellPersonal);
                                                celCus7.setCellStyle(my_style);
                                                Cell celTokenSN = rowCusDetail.createCell((short) 18);
                                                celTokenSN.setCellValue(sTokenSN);
                                                celTokenSN.setCellStyle(my_style);
                                                
                                                Cell celProfileStatus = rowCusDetail.createCell((short) 19);
                                                celProfileStatus.setCellValue(sPastProfileStatus);
                                                celProfileStatus.setCellStyle(my_style);
                                                Cell celAgentCode = rowCusDetail.createCell((short) 20);
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

                                                    Cell cellBranch1 = rowDecline.createCell((short) 2);
                                                    cellBranch1.setCellValue(temp1.BRANCH_NAME);
                                                    cellBranch1.setCellStyle(my_styleDecBranch);

                                                    String sTAX_CODE = temp1.ENTERPRISE_ID;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                        sTAX_CODE = CommonReferServlet.filterPrefixUIDLanguage(temp1.ENTERPRISE_ID, loginLanguage);
                                                    }
                                                    Cell cellBranch4 = rowDecline.createCell((short) 3);
                                                    cellBranch4.setCellValue(sTAX_CODE);
                                                    cellBranch4.setCellStyle(styleText);

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
                                                    
                                                    Cell cellDec22 = rowDecline.createCell((short) 12);
                                                    Date dateRevoke = CommonFunction.convertStringToDate(temp1.REVOKED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateRevoke != null) {
                                                        cellDec22.setCellValue(dateRevoke);
                                                    } else {
                                                        cellDec22.setCellValue("");
                                                    }
                                                    cellDec22.setCellStyle(my_styleBranchDate);
                                                    
                                                    String sCERTIFICATION_SN = temp1.CERTIFICATION_SN;
                                                    Cell cellBranch19 = rowDecline.createCell((short) 13);
                                                    cellBranch19.setCellValue(sCERTIFICATION_SN);
                                                    cellBranch19.setCellStyle(styleText);

                                                    Cell cellBranch3 = rowDecline.createCell((short) 14);
                                                    cellBranch3.setCellValue(temp1.SERVICE_TYPE_DESC);
                                                    cellBranch3.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch2 = rowDecline.createCell((short) 15);
                                                    cellBranch2.setCellValue(temp1.CREATED_BY);
                                                    cellBranch2.setCellStyle(my_styleDecBranch);

                                                    String sP_ID = temp1.PERSONAL_ID;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                        sP_ID = CommonReferServlet.filterPrefixUIDLanguage(temp1.PERSONAL_ID, loginLanguage);
                                                    }
                                                    Cell cellBranch6 = rowDecline.createCell((short) 16);
                                                    cellBranch6.setCellValue(sP_ID);
                                                    cellBranch6.setCellStyle(styleText);

                                                    Cell cellBranch7 = rowDecline.createCell((short) 17);
                                                    cellBranch7.setCellValue(temp1.PERSONAL_NAME);
                                                    cellBranch7.setCellStyle(my_styleDecBranch);
                                                    
                                                    String valueTokenSN = temp1.TOKEN_SN;
                                                    if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                    Cell cellDec21 = rowDecline.createCell((short) 18);
                                                    cellDec21.setCellValue(valueTokenSN);
                                                    cellDec21.setCellStyle(styleText);
                                                    
                                                    Cell cellProfileStatus = rowDecline.createCell((short) 19);
                                                    cellProfileStatus.setCellValue(temp1.PAST_FILE_MANAGER_STATE_DESC);
                                                    cellProfileStatus.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellAgentCode = rowDecline.createCell((short) 20);
                                                    cellAgentCode.setCellValue(temp1.PAST_BRANCH_DESC);
                                                    cellAgentCode.setCellStyle(my_styleDecBranch);
                                                    i++;
                                                }
                                            }*/
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
                            case "exportreportcertlist_20": {
                                //<editor-fold defaultstate="collapsed" desc="exportreportcertlist_20">
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
                                    CommonFunction.LogDebugString(logServlet, "ExportReportCertList", "Year: " + FromCreateDate
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
                                        sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                        sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
//                                        sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STATE);
                                        sheet.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                        sheet.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                        sheet.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_FORMFACTOR);
                                        sheet.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                        sheet.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                        sheet.setColumnWidth(10, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
                                        sheet.setColumnWidth(11, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_GEN);
                                        sheet.setColumnWidth(12, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CANCEL);
                                        sheet.setColumnWidth(13, Definitions.CONFIG_EXPORT_QUICK_WIDTH_NUM_DATE_CANCEL);
                                        sheet.setColumnWidth(14, Definitions.CONFIG_EXPORT_QUICK_WIDTH_NUM_DATE_CANCEL);
                                        sheet.setColumnWidth(15, Definitions.CONFIG_EXPORT_QUICK_WIDTH_NUM_DATE_CANCEL);
                                        
//                                        sheet.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
//                                        sheet.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROVINCE);
//                                        sheet.setColumnWidth(17, Definitions.CONFIG_EXPORT_QUICK_WIDTH_NUM_DATE_CANCEL);
//                                        sheet.setColumnWidth(19, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_GEN);
//                                        sheet.setColumnWidth(20, 18*255);
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

                                        Cell cell5 = row.createCell((short) 3);
                                        cell5.setCellValue(sCellCompany);
                                        cell5.setCellStyle(my_style);

                                        Cell cell3 = row.createCell((short) 4);
                                        cell3.setCellValue(sCellMST);
                                        cell3.setCellStyle(my_style);

                                        Cell cell7 = row.createCell((short) 5);
                                        cell7.setCellValue(sCellPesonal);
                                        cell7.setCellStyle(my_style);

                                        Cell cell6 = row.createCell((short) 6);
                                        cell6.setCellValue(sCellCMND);
                                        cell6.setCellStyle(my_style);
                                        
                                        Cell cell11 = row.createCell((short) 7);
                                        cell11.setCellValue(sCellFormFactor);
                                        cell11.setCellStyle(my_style);
                                        
                                        Cell cell10 = row.createCell((short) 8);
                                        cell10.setCellValue(sCellRequestType);
                                        cell10.setCellStyle(my_style);

                                        Cell cell9 = row.createCell((short) 9);
                                        cell9.setCellValue(sCellProfile);
                                        cell9.setCellStyle(my_style);
                                        
                                        Cell cell12 = row.createCell((short) 10);
                                        cell12.setCellValue(sCellDateCreate);
                                        cell12.setCellStyle(my_style);

                                        Cell cell1Effective = row.createCell((short) 11);
                                        cell1Effective.setCellValue(sCellDateEffective);
                                        cell1Effective.setCellStyle(my_style);

                                        Cell cell13 = row.createCell((short) 12);
                                        cell13.setCellValue(sCellDateCancel);
                                        cell13.setCellStyle(my_style);

                                        Cell cell14 = row.createCell((short) 13);
                                        cell14.setCellValue(sCellNUM_DATE_CANCEL);
                                        cell14.setCellStyle(my_style);

                                        Cell cell8 = row.createCell((short) 14);
                                        cell8.setCellValue("Note");
                                        cell8.setCellStyle(my_style);
                                        
                                        Cell cell81 = row.createCell((short) 15);
                                        cell81.setCellValue("Ghi chú");
                                        cell81.setCellStyle(my_style);

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
                                            my_styleBranch.setFont(fontBranch);

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

                                            Cell cellBranch5 = row1.createCell((short) 3);
                                            cellBranch5.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.COMPANY_NAME));
                                            cellBranch5.setCellStyle(my_styleBranch);

                                            String sMSTAndBUDGET_CODE = EscapeUtils.CheckTextNull(rsReportBranch1.ENTERPRISE_ID);
                                            Cell cellBranch3 = row1.createCell((short) 4);
                                            cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                            cellBranch3.setCellStyle(styleText);

                                            Cell cellBranch7 = row1.createCell((short) 5);
                                            cellBranch7.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PERSONAL_NAME));
                                            cellBranch7.setCellStyle(my_styleBranch);

                                            String sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.PERSONAL_ID);
                                            Cell cellBranch6 = row1.createCell((short) 6);
                                            cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                            cellBranch6.setCellStyle(styleText);
                                            
                                            Cell cellBranch11 = row1.createCell((short) 7);
                                            cellBranch11.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.FORM_FACTOR_DESC));
                                            cellBranch11.setCellStyle(my_styleBranch);
                                            
                                            Cell cellAttrType = row1.createCell((short) 8);
                                            cellAttrType.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_ATTR_TYPE_DESC));
                                            cellAttrType.setCellStyle(my_styleBranch);

                                            Cell cellBranch9 = row1.createCell((short) 9);
                                            cellBranch9.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_PROFILE_NAME));
                                            cellBranch9.setCellStyle(my_styleBranch);
                                            
                                            Date date = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.CREATED_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                            Cell cellBranch12 = row1.createCell((short) 10);
                                            if(date != null) {
                                                cellBranch12.setCellValue(date);
                                            } else {
                                                cellBranch12.setCellValue("");
                                            }
                                            cellBranch12.setCellStyle(my_styleBranchDate);

                                            Cell cellValueEffective = row1.createCell((short) 11);
                                            Date dateEffective = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.EFFECTIVE_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                            if(dateEffective != null) {
                                                cellValueEffective.setCellValue(dateEffective);
                                            } else {
                                                cellValueEffective.setCellValue("");
                                            }
                                            cellValueEffective.setCellStyle(my_styleBranchDate);

                                            Cell cellBranch13 = row1.createCell((short) 12);
                                            Date dateRevoke = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.REVOKED_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                            if(dateRevoke != null) {
                                                cellBranch13.setCellValue(dateRevoke);
                                            } else {
                                                cellBranch13.setCellValue("");
                                            }
                                            cellBranch13.setCellStyle(my_styleBranchDate);

                                            Cell cellBranch14 = row1.createCell((short) 13);
                                            cellBranch14.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.NUMBER_DELETED));
                                            cellBranch14.setCellStyle(my_styleBranch);
                                            
                                            Cell cellNote = row1.createCell((short) 14);
                                            cellNote.setCellValue("");
                                            cellNote.setCellStyle(my_styleBranch);
                                            
                                            Cell cellGhichu = row1.createCell((short) 15);
                                            cellGhichu.setCellValue("");
                                            cellGhichu.setCellStyle(my_styleBranch);
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
                        }
                    }
                } else if (sOutInner == 2) {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "#0";
                } else {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "#0";
                }
            } catch (NumberFormatException | SQLException e) {
                CommonFunction.LogExceptionServlet(logServlet, e.getMessage(), e);
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
            CommonFunction.LogExceptionServlet(logServlet, e.getMessage(), e);
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
            CommonFunction.LogExceptionServlet(logServlet, e.getMessage(), e);
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
