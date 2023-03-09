/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import java.io.FileInputStream;
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
import vn.ra.object.CERTIFICATION;
import vn.ra.object.ProfileContactInfoJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import vn.ra.process.CommonFunction;
import vn.ra.process.CommonReferServlet;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author USER
 */
public class ContactProfileImport extends HttpServlet {
    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ContactProfileImport.class.getName());
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
                    String sessLanguage = sessionsa.getAttribute("sessVN").toString().trim();
                    String USER_LOG_ID = EscapeUtils.CheckTextNull(sessionsa.getAttribute("UserID").toString().trim());
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
                                if (sessionsa.getAttribute("SessAgentID").toString().trim().equals(Definitions.CONFIG_AGENT_ROOT)) {
//                                    Config conf = new Config();
                                    String sColumnSTT = "STT";
                                    String sColumnpPrefixDN = "PREFIX DOANH NGHIEP";
                                    String sColumnpUIDDN = "UID DOANH NGHIEP";
                                    String sColumnpPrefixCN = "PREFIX CA NHAN";
                                    String sColumnpUIDCN = "UID CA NHAN";
                                    String sColumnADDRESS = "DIA CHI";
                                    String sColumnREPRESENTATIVE = "NGUOI DAI DIEN";
                                    String sColumnPOSITION = "CHUC VU";
                                    String sColumnCONTACT_NAME = "NGUOI LIEN HE";
                                    String sColumnPHONE_NUMBER = "SDT NGUOI LIEN HE";
                                    String sColumnEMAIL = "EMAIL NGUOI LIEN HE";
                                    if("0".equals(sessLanguage)){
                                        sColumnSTT = "No";
                                        sColumnpPrefixDN = "PREFIX_UID_ENTERPRISE";
                                        sColumnpUIDDN = "UID_ENTERPRISE";
                                        sColumnpPrefixCN = "PREFIX_UID_PERSONAL";
                                        sColumnpUIDCN = "UID_PERSONAL";
                                        sColumnADDRESS = "ADDRESS";
                                        sColumnREPRESENTATIVE = "REPRESENTATIVE";
                                        sColumnPOSITION = "POSITION";
                                        sColumnCONTACT_NAME = "CONTACT_NAME";
                                        sColumnPHONE_NUMBER = "PHONE_NUMBER";
                                        sColumnEMAIL = "EMAIL";
                                    }
                                    int success = 0;
                                    int failed = 0;
                                    String strFailedNoRecord = "";
                                    String strFailedNoToken = "";
                                    String strFailedPrefix = "";
                                    ArrayList dataHolder;
                                    ArrayList cellStoreArrayList;
                                    int indexOfSTT = 100;
                                    int indexOfPrefixDN = 100;
                                    int indexOfUIDDN = 100;
                                    int indexOfPrefixCN = 100;
                                    int indexOfUIDCN = 100;
                                    int indexOfADDRESS = 100;
                                    int indexOfREPRESENTATIVE = 100;
                                    int indexOfPOSITION = 100;
                                    int indexOfCONTACT_NAME = 100;
                                    int indexOfPHONE_NUMBER = 100;
                                    int indexOfEMAIL = 100;
                                    if (isXLSX > 0) {
                                        dataHolder = readExcelImportContactXLSX(fileUploaded);
                                    } else {
                                        dataHolder = readExcelImportContactXLS(fileUploaded);
                                    }
                                    //<editor-fold defaultstate="collapsed" desc="Import Process">
                                    cellStoreArrayList = (ArrayList) dataHolder.get(0);
                                    for (int i = 0; i < cellStoreArrayList.size(); i++) {
                                        if (sColumnSTT.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfSTT = i;
                                        }
                                        if (sColumnpPrefixDN.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfPrefixDN = i;
                                        }
                                        if (sColumnpUIDDN.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfUIDDN = i;
                                        }
                                        if (sColumnpPrefixCN.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfPrefixCN = i;
                                        }
                                        if (sColumnpUIDCN.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfUIDCN = i;
                                        }
                                        if (sColumnREPRESENTATIVE.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfREPRESENTATIVE = i;
                                        }
                                        if (sColumnPOSITION.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfPOSITION = i;
                                        }
                                        if (sColumnCONTACT_NAME.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfCONTACT_NAME = i;
                                        }
                                        if (sColumnPHONE_NUMBER.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfPHONE_NUMBER = i;
                                        }
                                        if (sColumnEMAIL.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfEMAIL = i;
                                        }
                                        if (sColumnADDRESS.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfADDRESS = i;
                                        }
                                    }
                                    boolean booFailColumnName = true;
                                    String sValueFailColumnName = "OK";
                                    if (indexOfSTT == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_STT;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfPrefixDN == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_MST;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfUIDDN == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_MNS;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfPrefixCN == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_MNS;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfUIDCN == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_CMND;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfREPRESENTATIVE == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_CCCD;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfPOSITION == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_CCCD;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfCONTACT_NAME == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_CCCD;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfPHONE_NUMBER == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_CCCD;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfEMAIL == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_CCCD;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfADDRESS == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_CCCD;
                                        booFailColumnName = false;
                                    }
                                    if (booFailColumnName == true) {
                                        for (int i = 1; i < dataHolder.size(); i++) {
                                            cellStoreArrayList = (ArrayList) dataHolder.get(i);
                                            String strSTT;
                                            String strPrefixDN;
                                            String strUIDDN;
                                            String strPrefixCN;
                                            String strUIDCN;
                                            String strRepresentative;
                                            String strPosition;
                                            String strContactName;
                                            String strEmail;
                                            String strPhone;
                                            String strAddress;
                                            strSTT = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfSTT).toString(), true);
                                            strPrefixDN = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfPrefixDN).toString(), false);
                                            strUIDDN = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfUIDDN).toString(), false);
                                            strPrefixCN = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfPrefixCN).toString(), false);
                                            strUIDCN = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfUIDCN).toString(), false);
                                            strRepresentative = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfREPRESENTATIVE).toString(), false);
                                            strPosition = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfPOSITION).toString(), false);
                                            strContactName = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCONTACT_NAME).toString(), false);
                                            strPhone = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfPHONE_NUMBER).toString(), false);
                                            strEmail = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfEMAIL).toString(), false);
                                            strAddress = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfADDRESS).toString(), false);
                                            if ((!"".equals(strPrefixDN) && !"".equals(strUIDDN)) || (!"".equals(strPrefixCN) && !"".equals(strUIDCN)))
                                            {
                                                String sVALID_CODE = "OK";
                                                if(!"".equals(strPrefixDN)){
                                                    if(CommonReferServlet.checkPrefixEnterpriseVN(strPrefixDN) == false) {
                                                        sVALID_CODE = "PREFIX_INVALID";
                                                    }
                                                }
                                                if(!"".equals(strPrefixCN)){
                                                    if(CommonReferServlet.checkPrefixPersonalVN(strPrefixCN) == false) {
                                                        sVALID_CODE = "PREFIX_INVALID";
                                                    }
                                                }
                                                if(sVALID_CODE.equals("OK")) {
                                                    //<editor-fold defaultstate="collapsed" desc="### Process">
    //                                                String[] sUIDResult = new String[2];
    //                                                CommonReferServlet.collectFieldToUID(strPrefixDN, strUIDDN, strPrefixCN, strUIDCN, strHC, strCCCD, sUIDResult);
                                                    String sEnterpriseCert = "";
                                                    String sPersonalCert = "";
                                                    if(!"".equals(strPrefixDN) && !"".equals(strUIDDN)) {
                                                        sEnterpriseCert = CommonReferServlet.convertPrefixVNToEN(strPrefixDN + ":" + strUIDDN, true);
                                                    }
                                                    if(!"".equals(strPrefixCN) && !"".equals(strUIDCN)) {
                                                        sPersonalCert = CommonReferServlet.convertPrefixVNToEN(strPrefixCN + ":" + strUIDCN, false);
                                                    }
                                                    CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                                    int[] inResult = new int[1];
                                                    db.S_BO_UUID_GET_CERTIFICATION_ID(sEnterpriseCert, sPersonalCert, "", rsCert, inResult);
                                                    if(inResult[0] == 0 && rsCert[0].length > 0) {
                                                        ObjectMapper oMapperParse = new ObjectMapper();
                                                        for(CERTIFICATION rsItem : rsCert[0]) {
                                                            ProfileContactInfoJson profileContact = new ProfileContactInfoJson();
                                                            String sPROFILE_CONTACT_INFO = rsItem.PROFILE_CONTACT_INFO;
                                                            if(!"".equals(sPROFILE_CONTACT_INFO)) {
                                                                ProfileContactInfoJson profileContactOld = oMapperParse.readValue(sPROFILE_CONTACT_INFO, ProfileContactInfoJson.class);
                                                                if("[NULL]".equals(strRepresentative)){
                                                                    profileContact.RepresentativeName = profileContactOld.RepresentativeName;
                                                                } else {
                                                                    profileContact.RepresentativeName = CommonFunction.replaceCharaterSpecialJson(strRepresentative, true);
                                                                }
                                                                if("[NULL]".equals(strPhone)){
                                                                    profileContact.RepresentativePhone = profileContactOld.RepresentativePhone;
                                                                } else {
                                                                    profileContact.RepresentativePhone = strPhone;
                                                                }
                                                                if("[NULL]".equals(strEmail)){
                                                                    profileContact.RepresentativeEmail = profileContactOld.RepresentativeEmail;
                                                                } else {
                                                                    profileContact.RepresentativeEmail = strEmail;
                                                                }
                                                                if("[NULL]".equals(strContactName)){
                                                                    profileContact.ContactName = profileContactOld.ContactName;
                                                                } else {
                                                                    profileContact.ContactName = CommonFunction.replaceCharaterSpecialJson(strContactName, true);
                                                                }
                                                                if("[NULL]".equals(strPosition)){
                                                                    profileContact.Position = profileContactOld.Position;
                                                                } else {
                                                                    profileContact.Position = CommonFunction.replaceCharaterSpecialJson(strPosition, true);
                                                                }
                                                                if("[NULL]".equals(strAddress)){
                                                                    profileContact.Address = profileContactOld.Address;
                                                                } else {
                                                                    profileContact.Address = CommonFunction.replaceCharaterSpecialJson(strAddress, true);
                                                                }
                                                            } else {
                                                                if("[NULL]".equals(strRepresentative)) {
                                                                    strRepresentative = "";
                                                                }
                                                                if("[NULL]".equals(strPhone)) {
                                                                    strPhone = "";
                                                                }
                                                                if("[NULL]".equals(strEmail)) {
                                                                    strEmail = "";
                                                                }
                                                                if("[NULL]".equals(strContactName)) {
                                                                    strContactName = "";
                                                                }
                                                                if("[NULL]".equals(strPosition)) {
                                                                    strPosition = "";
                                                                }
                                                                if("[NULL]".equals(strAddress)) {
                                                                    strAddress = "";
                                                                }
                                                                profileContact.RepresentativeName = CommonFunction.replaceCharaterSpecialJson(strRepresentative, true);
                                                                profileContact.RepresentativePhone = strPhone;
                                                                profileContact.RepresentativeEmail = strEmail;
                                                                profileContact.ContactName = CommonFunction.replaceCharaterSpecialJson(strContactName, true);
                                                                profileContact.Position = CommonFunction.replaceCharaterSpecialJson(strPosition, true);
                                                                profileContact.Address = CommonFunction.replaceCharaterSpecialJson(strAddress, true);
                                                            }
                                                            db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(rsItem.ID, oMapperParse.writeValueAsString(profileContact),USER_LOG_ID);
                                                        }
                                                        success = success + 1;
                                                    } else {
                                                        strFailedNoToken = strFailedNoToken + strSTT + ", ";
                                                        failed = failed + 1;
                                                    }
                                                    //</editor-fold>
                                                } else {
                                                    strFailedPrefix = strFailedPrefix + strSTT + ", ";
                                                    failed = failed + 1;
                                                }
                                            } else {
                                                strFailedNoRecord = strFailedNoRecord + strSTT + ", ";
                                                failed = failed + 1;
                                            }
                                        }
                                        String sSum="";
                                        if (!"".equals(strFailedNoToken) || !"".equals(strFailedNoRecord) || !"".equals(strFailedPrefix))
                                        {
                                            if(!"".equals(strFailedNoToken)) {
                                                sSum = "Error - No data found in system - STT: " + strFailedNoToken + "\n";
                                            }
                                            if(!"".equals(strFailedPrefix)) {
                                                sSum = "Error - Prefix UID not exists in system - STT: " + strFailedPrefix + "\n";
                                            }
                                            if(!"".equals(strFailedNoRecord)) {
                                                sSum = "Error - Empty record - STT: " + strFailedNoRecord;
                                            }
                                            sessionsa.setAttribute("sessProfileContactImportFailed", sSum);
                                        }
                                        strView = "0###" + String.valueOf(success) + "###" + String.valueOf(failed) + "###" + sSum;
                                    } else {
                                        strView = "1###" + sValueFailColumnName;
                                    }
                                    //</editor-fold>
                                }
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
    
    public static ArrayList readExcelImportContactXLSX(String sLinkFile)
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
                XSSFCell cellPrefixDN = myRow.getCell(1);
                XSSFCell cellUIDDN = myRow.getCell(2);
                XSSFCell cellPrefixCN = myRow.getCell(3);
                XSSFCell cellUIDCN = myRow.getCell(4);
                XSSFCell cellRepresentative = myRow.getCell(5);
                XSSFCell cellPosition = myRow.getCell(6);
                XSSFCell cellContactName = myRow.getCell(7);
                XSSFCell cellPhone = myRow.getCell(8);
                XSSFCell cellEmail = myRow.getCell(9);
                XSSFCell cellAddress = myRow.getCell(10);
                if (CommonFunction.CheckCellXSSFEmpty(cellSTT) == null && CommonFunction.CheckCellXSSFEmpty(cellPrefixDN) == null
                    && CommonFunction.CheckCellXSSFEmpty(cellUIDDN) == null && CommonFunction.CheckCellXSSFEmpty(cellPrefixCN) == null
                    && CommonFunction.CheckCellXSSFEmpty(cellUIDCN) == null
                    && CommonFunction.CheckCellXSSFEmpty(cellRepresentative) == null && CommonFunction.CheckCellXSSFEmpty(cellPosition) == null
                    && CommonFunction.CheckCellXSSFEmpty(cellContactName) == null && CommonFunction.CheckCellXSSFEmpty(cellPhone) == null
                    && CommonFunction.CheckCellXSSFEmpty(cellEmail) == null && CommonFunction.CheckCellXSSFEmpty(cellAddress) == null) {

                } else {
                    if (cellSTT == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if(cellSTT.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellSTT.getNumericCellValue());
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellSTT.toString()));
                        }
                    }
                    if (cellPrefixDN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if(cellPrefixDN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellPrefixDN.getNumericCellValue());
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellPrefixDN.toString()));
                        }
                    }
                    if (cellUIDDN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if(cellUIDDN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellUIDDN.getNumericCellValue());
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellUIDDN.toString()));
                        }
                    }
                    if (cellPrefixCN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if(cellPrefixCN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellPrefixCN.getNumericCellValue());
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellPrefixCN.toString()));
                        }
                    }
                    if (cellUIDCN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if(cellUIDCN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellUIDCN.getNumericCellValue());
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellUIDCN.toString()));
                        }
                    }
                    if (cellRepresentative == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellRepresentative.toString()));
                    }
                    if (cellPosition == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellPosition.toString()));
                    }
                    if (cellContactName == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellContactName.toString()));
                    }
                    if (cellEmail == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellEmail.toString()));
                    }
                    if (cellPhone == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if(cellPhone.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellPhone.getNumericCellValue());
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellPhone.toString()));
                        }
                    }
                    if (cellAddress == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellAddress.toString()));
                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            log.error("readExcelImportPushXLSX: " + e.getMessage() + ".\n-----------------------------------", e);
        }
        return cellArrayLisstHolder;
    }
    
    public static ArrayList readExcelImportContactXLS(String sLinkFile)
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
                HSSFCell cellPrefiDN = myRow.getCell(1);
                HSSFCell cellUIDDN = myRow.getCell(2);
                HSSFCell cellPrefixCN = myRow.getCell(3);
                HSSFCell cellUIDCN = myRow.getCell(4);
                HSSFCell cellRepresentative = myRow.getCell(5);
                HSSFCell cellPosition = myRow.getCell(6);
                HSSFCell cellContactName = myRow.getCell(7);
                HSSFCell cellPhone = myRow.getCell(8);
                HSSFCell cellEmail = myRow.getCell(9);
                HSSFCell cellAddress = myRow.getCell(10);
                if (CommonFunction.CheckCellHSSFEmpty(cellSTT) == null && CommonFunction.CheckCellHSSFEmpty(cellPrefiDN) == null
                    && CommonFunction.CheckCellHSSFEmpty(cellUIDDN) == null && CommonFunction.CheckCellHSSFEmpty(cellPrefixCN) == null
                    && CommonFunction.CheckCellHSSFEmpty(cellUIDCN) == null
                    && CommonFunction.CheckCellHSSFEmpty(cellRepresentative) == null && CommonFunction.CheckCellHSSFEmpty(cellPosition) == null
                    && CommonFunction.CheckCellHSSFEmpty(cellContactName) == null && CommonFunction.CheckCellHSSFEmpty(cellPhone) == null
                    && CommonFunction.CheckCellHSSFEmpty(cellEmail) == null && CommonFunction.CheckCellHSSFEmpty(cellAddress) == null) {

                } else {
                    if (cellSTT == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if(cellSTT.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellSTT.getNumericCellValue());
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellSTT.toString()));
                        }
                    }
                    if (cellPrefiDN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if(cellPrefiDN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellPrefiDN.getNumericCellValue());
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellPrefiDN.toString()));
                        }
                    }
                    if (cellUIDDN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if(cellUIDDN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellUIDDN.getNumericCellValue());
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellUIDDN.toString()));
                        }
                    }
                    if (cellPrefixCN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if(cellPrefixCN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellPrefixCN.getNumericCellValue());
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellPrefixCN.toString()));
                        }
                    }
                    if (cellUIDCN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if(cellUIDCN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellUIDCN.getNumericCellValue());
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellUIDCN.toString()));
                        }
                    }
                    if (cellRepresentative == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellRepresentative.toString()));
                    }
                    if (cellPosition == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellPosition.toString()));
                    }
                    if (cellContactName == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellContactName.toString()));
                    }
                    if (cellEmail == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellEmail.toString()));
                    }
                    if (cellPhone == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if(cellPhone.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellPhone.getNumericCellValue());
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellPhone.toString()));
                        }
                    }
                    if (cellAddress == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CommonFunction.CheckReplaceImport(cellAddress.toString()));
                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            log.error("readExcelImportContactXLS: " + e.getMessage() + ".\n-----------------------------------", e);
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
