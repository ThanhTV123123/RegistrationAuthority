/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vn.ra.object.BACKOFFICE_USER;
import vn.ra.object.BRANCH;
import vn.ra.object.TOKEN_IMPORT;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.EncodeSOPIN;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
public class TokenImport extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TokenImport.class.getName());

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
//            String AGENT_ID_LOG = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SessAgentID").toString().trim());
//            String SessUserAgentID = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SessUserAgentID").toString().trim());
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
                    String USER_LOG = EscapeUtils.CheckTextNull(sessionsa.getAttribute("sUserID").toString().trim());
//                    String loginAgentID = sessionsa.getAttribute("SessAgentID").toString().trim();
                    String sTOKEN_VERSION_GET = EscapeUtils.CheckTextNull(sessionsa.getAttribute("TOKEN_VERSION").toString().trim());
                    int sImportBranchID = Integer.parseInt(EscapeUtils.CheckTextNull(sessionsa.getAttribute("ImportBranchID").toString().trim()));
                    if ("".equals(sTOKEN_VERSION_GET)) {
                        sTOKEN_VERSION_GET = "1";
                    }
                    int sTOKEN_VERSION = Integer.parseInt(sTOKEN_VERSION_GET);
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
                                    boolean checkBranchValid = false;
                                    BRANCH[][] rsBranch = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystemAgency");
                                    if (rsBranch[0].length > 0) {
                                        for (BRANCH item : rsBranch[0]) {
                                            if (String.valueOf(item.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                if (item.ID != 1) {
                                                    if(item.ID == sImportBranchID){
                                                        checkBranchValid = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if(checkBranchValid == true) {
                                        BACKOFFICE_USER[][] rsUser = new BACKOFFICE_USER[1][];
                                        db.S_BO_USER_GET_BY_USERNAME(USER_LOG, rsUser);
                                        int pUSER_ID = 0;
                                        if(rsUser[0].length > 0) {
                                            pUSER_ID = rsUser[0][0].ID;
                                            Config conf = new Config();
                                            String sColumnTokenSN = conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_TOKEN_SN);
                                            String sColumnTokenSOPIN = conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_TOKEN_SOPIN);
                                            int success = 0;
                                            int failed = 0;
                                            int updated = 0;
                                            ArrayList dataHolder;
                                            ArrayList cellStoreArrayList;
                                            int indexOfVendorCode = 100;
                                            int indexOfICCID = 100;
                                            String strFailedAgency = "";
        //                                    String strFailed_Format = "";
                                            String strFailed_Empty = "";
                                            String strFailed_SOPINEncript = "";
                                            if (isXLSX > 0) {
                                                dataHolder = CommonFunction.readExcelImportTokenXLSX(fileUploaded);
                                            } else {
                                                dataHolder = CommonFunction.readExcelImportTokenXLS(fileUploaded);
                                            }
                                            cellStoreArrayList = (ArrayList) dataHolder.get(0);
                                            for (int i = 0; i < cellStoreArrayList.size(); i++) {
                                                if (sColumnTokenSN.equals(cellStoreArrayList.get(i).toString().trim())) {
                                                    indexOfVendorCode = i;
                                                }
                                                if (sColumnTokenSOPIN.equals(cellStoreArrayList.get(i).toString().trim())) {
                                                    indexOfICCID = i;
                                                }
                                            }
                                            boolean booFailColumnName = true;
                                            String sValueFailColumnName = "OK";
                                            if (indexOfVendorCode == 100) {
                                                sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                                booFailColumnName = false;
                                            }
                                            if (indexOfICCID == 100) {
                                                sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SOPIN;
                                                booFailColumnName = false;
                                            }
                                            if (booFailColumnName == true) {
                                                int sRecordNo = 1;
                                                String sSopinClearEnabled = conf.GetPropertybyCode(Definitions.CONFIG_IMPORT_TOKEN_SOPIN_CLEAR_ENABLED);
                                                EncodeSOPIN encript = new EncodeSOPIN();
                                                String strTokenID;
                                                String strSOPIN;
                                                TOKEN_IMPORT[][] rsToken = new TOKEN_IMPORT[1][];
                                                ArrayList<TOKEN_IMPORT> tempList = new ArrayList<>();
                                                for (int i = 1; i < dataHolder.size(); i++) {
                                                    sRecordNo = sRecordNo + 1;
                                                    cellStoreArrayList = (ArrayList) dataHolder.get(i);
                                                    strTokenID = cellStoreArrayList.get(indexOfVendorCode).toString();
                                                    strSOPIN = cellStoreArrayList.get(indexOfICCID).toString();
                                                    if (!"".equals(strTokenID) && !"".equals(strSOPIN) && !"0".equals(strTokenID)) {
                                                        if("1".equals(sSopinClearEnabled)) {
                                                            strSOPIN = encript.encode(EscapeUtils.escapeHtml(strSOPIN));
                                                        }
                                                        if(CommonFunction.checkSOPINEncode(strSOPIN) == true) {
                                                            TOKEN_IMPORT tempItem = new TOKEN_IMPORT();
                                                            tempItem.TOKEN_SN = strTokenID;
                                                            tempItem.SOPIN = strSOPIN;
                                                            tempItem.TOKEN_VERSION_ID = sTOKEN_VERSION;
                                                            tempItem.BRANCH_ID = sImportBranchID;
                                                            tempItem.USER_BY = pUSER_ID;
                                                            tempList.add(tempItem);
                                                        } else {
                                                            strFailed_SOPINEncript = strFailed_SOPINEncript + String.valueOf(sRecordNo) + ", ";
                                                            failed = failed + 1;
                                                        }
                                                    } else {
                                                        strFailed_Empty = strFailed_Empty + String.valueOf(sRecordNo) + ", ";
                                                        failed = failed + 1;
                                                    }
                                                }
                                                boolean isFailed = false;
                                                if (!"".equals(strFailedAgency) || !"".equals(strFailed_SOPINEncript)
                                                    || !"".equals(strFailed_Empty))
                                                {
                                                    isFailed = true;
                                                    String sSum="";
                                                    if(!"".equals(strFailedAgency)){
                                                        sSum = "- The Agency Is Invalid - Line Number: " + strFailedAgency + "\n";
                                                    }
                                                    if(!"".equals(strFailed_SOPINEncript)) {
                                                        sSum = "- TOKEN_SOPIN encoding is not correct - Line Number: " + strFailed_SOPINEncript + "\n";
                                                    }
                                                    if(!"".equals(strFailed_Empty)) {
                                                        sSum = "- Token information cannot be empty - Line Number: " + strFailed_Empty;
                                                    }
                                                    sessionsa.setAttribute("sessTokenImportFailed", sSum);
                                                }
                                                if(isFailed == false) {
                                                    rsToken[0] = new TOKEN_IMPORT[tempList.size()];
                                                    rsToken[0] = tempList.toArray(rsToken[0]);
                                                    sessionsa.setAttribute("sessTokenImportBundle", rsToken);
                                                } else {
                                                    sessionsa.setAttribute("sessTokenImportBundle", null);
                                                }
                                                strView = "0###" + String.valueOf(success) + "###" + String.valueOf(failed)
                                                        + "###" + String.valueOf(updated) + "###" + strFailedAgency+strFailed_SOPINEncript+strFailed_Empty;
                                            } else {
                                                strView = "1###" + sValueFailColumnName;
                                            }

    //                                        if (isXLSX > 0) {
    //                                            //<editor-fold defaultstate="collapsed" desc="XLSX">
    //                                            dataHolder = CommonFunction.readExcelImportTokenXLSX(fileUploaded);
    //                                            cellStoreArrayList = (ArrayList) dataHolder.get(0);
    //                                            for (int i = 0; i < cellStoreArrayList.size(); i++) {
    //                                                if (sColumnTokenSN.equals(cellStoreArrayList.get(i).toString().trim())) {
    //                                                    indexOfVendorCode = i;
    //                                                }
    //                                                if (sColumnTokenSOPIN.equals(cellStoreArrayList.get(i).toString().trim())) {
    //                                                    indexOfICCID = i;
    //                                                }
    //                                            }
    //                                            boolean booFailColumnName = true;
    //                                            String sValueFailColumnName = "OK";
    //                                            if (indexOfVendorCode == 100) {
    //                                                sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
    //                                                booFailColumnName = false;
    //                                            }
    //                                            if (indexOfICCID == 100) {
    //                                                sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SOPIN;
    //                                                booFailColumnName = false;
    //                                            }
    //                                            if (booFailColumnName == true) {
    //                                                int sRecordNo = 1;
    //                                                String sSopinClearEnabled = conf.GetPropertybyCode(Definitions.CONFIG_IMPORT_TOKEN_SOPIN_CLEAR_ENABLED);
    //                                                EncodeSOPIN encript = new EncodeSOPIN();
    //                                                CallableStatement proc_stmt = null;
    //                                                ConnectDatabase dbNew = new ConnectDatabase();
    //                                                Connection conns = dbNew.OpenDatabase();
    //                                                String strTokenID;
    //                                                String strSOPIN;
    //                                                for (int i = 1; i < dataHolder.size(); i++) {
    //                                                    sRecordNo = sRecordNo + 1;
    //                                                    cellStoreArrayList = (ArrayList) dataHolder.get(i);
    //                                                    strTokenID = cellStoreArrayList.get(indexOfVendorCode).toString();
    //                                                    strSOPIN = cellStoreArrayList.get(indexOfICCID).toString();
    //                                                    if (!"".equals(strTokenID) && !"".equals(strSOPIN) && !"0".equals(strTokenID)) {
    //                                                        if("1".equals(sSopinClearEnabled)) {
    //                                                            strSOPIN = encript.encode(EscapeUtils.escapeHtml(strSOPIN));
    //                                                        }
    //                                                        if(CommonFunction.checkSOPINEncode(strSOPIN) == true) {
    //                                                            try {
    //                                                                S_BO_TOKEN_IMPORT_MULTIPLE(conns, proc_stmt, strTokenID, strSOPIN, sTOKEN_VERSION, sImportBranchID, pUSER_ID);
    //                                                                success = success + 1;
    //                                                            } catch (Exception e) {
    //                                                                int paramUpdate = S_BO_TOKEN_IMPORT_UPDATE_INNER(conns, proc_stmt, strTokenID,
    //                                                                    strSOPIN, sTOKEN_VERSION, sImportBranchID, USER_LOG);
    //                                                                if (paramUpdate == 0) {
    //                                                                    updated = updated + 1;
    //                                                                } else {
    //                                                                    strFailedAgency = strFailedAgency + String.valueOf(sRecordNo) + ", ";
    //    //                                                                log.info("TOKEN_SN: " + strTokenID + ". The Agency Is Invalid\n-----------------------------------");
    //                                                                    failed = failed + 1;
    //                                                                }
    //                                                            }
    //                                                        } else {
    //                                                            strFailed_SOPINEncript = strFailed_SOPINEncript + String.valueOf(sRecordNo) + ", ";
    //                                                            failed = failed + 1;
    //                                                        }
    //                                                    } else {
    //                                                        strFailed_Empty = strFailed_Empty + String.valueOf(sRecordNo) + ", ";
    //                                                        failed = failed + 1;
    //                                                    }
    //                                                }
    //                                                Connection[] temp_connection = new Connection[]{conns};
    //                                                dbNew.CloseDatabase(temp_connection);
    //                                                // || !"".equals(strFailed_Format)
    //                                                if (!"".equals(strFailedAgency) || !"".equals(strFailed_SOPINEncript)
    //                                                    || !"".equals(strFailed_Empty)) {
    //                                                    String sSum="";
    //                                                    if(!"".equals(strFailedAgency)){
    //                                                        sSum = "- The Agency Is Invalid - Line Number: " + strFailedAgency + "\n";
    //                                                    }
    //    //                                                if(!"".equals(strFailed_Format)) {
    //    //                                                    sSum = "- Incorrect format of TOKEN_SN: - Line Number: " + strFailed_Format + "\n";
    //    //                                                }
    //                                                    if(!"".equals(strFailed_SOPINEncript)) {
    //                                                        sSum = "- TOKEN_SOPIN encoding is not correct - Line Number: " + strFailed_SOPINEncript + "\n";
    //                                                    }
    //                                                    if(!"".equals(strFailed_Empty)) {
    //                                                        sSum = "- Token information cannot be empty - Line Number: " + strFailed_Empty;
    //                                                    }
    //                                                    sessionsa.setAttribute("sessTokenImportFailed", sSum);
    //                                                    CommonFunction.LogDebugString(log, "STR_VIEW-1", sSum);
    //                                                }
    //                                                //+strFailed_Format
    //                                                strView = "0###" + String.valueOf(success) + "###" + String.valueOf(failed)
    //                                                        + "###" + String.valueOf(updated) + "###" + strFailedAgency+strFailed_SOPINEncript+strFailed_Empty;
    //                                                CommonFunction.LogDebugString(log, "STR_VIEW-2", strView);
    //                                            } else {
    //                                                strView = "1###" + sValueFailColumnName;
    //                                            }
    //                                            //</editor-fold>
    //                                        } else {
    //                                            //<editor-fold defaultstate="collapsed" desc="XLS">
    //                                            dataHolder = CommonFunction.readExcelImportTokenXLS(fileUploaded);
    //                                            cellStoreArrayList = (ArrayList) dataHolder.get(0);
    //                                            for (int i = 0; i < cellStoreArrayList.size(); i++) {
    //                                                if (sColumnTokenSN.equals(cellStoreArrayList.get(i).toString().trim())) {
    //                                                    indexOfVendorCode = i;
    //                                                }
    //                                                if (sColumnTokenSOPIN.equals(cellStoreArrayList.get(i).toString().trim())) {
    //                                                    indexOfICCID = i;
    //                                                }
    //                                            }
    //                                            boolean booFailColumnName = true;
    //                                            String sValueFailColumnName = "OK";
    //                                            if (indexOfVendorCode == 100) {
    //                                                sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
    //                                                booFailColumnName = false;
    //                                            }
    //                                            if (indexOfICCID == 100) {
    //                                                sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SOPIN;
    //                                                booFailColumnName = false;
    //                                            }
    //                                            if (booFailColumnName == true) {
    //                                                int v = dataHolder.size();
    //                                                int sRecordNo = 1;
    //                                                String sSopinClearEnabled = conf.GetPropertybyCode(Definitions.CONFIG_IMPORT_TOKEN_SOPIN_CLEAR_ENABLED);
    //                                                EncodeSOPIN encript = new EncodeSOPIN();
    //                                                CallableStatement proc_stmt = null;
    //                                                ConnectDatabase dbNew = new ConnectDatabase();
    //                                                Connection conns = dbNew.OpenDatabase();
    //                                                CommonFunction.LogDebugString(log, "Performance", "BEGIN");
    //                                                String strTokenID;
    //                                                String strSOPIN;
    //                                                for (int i = 1; i < v; i++) {
    //                                                    sRecordNo = sRecordNo + 1;
    //                                                    cellStoreArrayList = (ArrayList) dataHolder.get(i);
    //                                                    strTokenID = cellStoreArrayList.get(indexOfVendorCode).toString();
    //                                                    strSOPIN = cellStoreArrayList.get(indexOfICCID).toString();
    //                                                    if (!"".equals(strTokenID) && !"".equals(strSOPIN) && !"0".equals(strTokenID)) {
    //                                                        if("1".equals(sSopinClearEnabled)) {
    //                                                            strSOPIN = encript.encode(EscapeUtils.escapeHtml(strSOPIN));
    //                                                        }
    //                                                        if(CommonFunction.checkSOPINEncode(strSOPIN) == true) {
    //                                                            try {
    //                                                                S_BO_TOKEN_IMPORT_MULTIPLE(conns, proc_stmt, strTokenID, strSOPIN, sTOKEN_VERSION, sImportBranchID, pUSER_ID);
    //                                                                success = success + 1;
    //                                                            } catch (Exception e) {
    //                                                                int paramUpdate = S_BO_TOKEN_IMPORT_UPDATE_INNER(conns, proc_stmt, strTokenID, strSOPIN, sTOKEN_VERSION, sImportBranchID, USER_LOG);
    //                                                                if (paramUpdate == 0) {
    //                                                                    updated = updated + 1;
    //                                                                } else {
    //                                                                    strFailedAgency = strFailedAgency + String.valueOf(sRecordNo) + ", ";
    //    //                                                                log.info("TOKEN_SN: " + strTokenID + ". The Agency Is Invalid\n-----------------------------------");
    //                                                                    failed = failed + 1;
    //                                                                }
    //                                                            }
    //                                                        } else {
    //                                                            strFailed_SOPINEncript = strFailed_SOPINEncript + String.valueOf(sRecordNo) + ", ";
    //                                                            failed = failed + 1;
    //                                                        }
    //    //                                                    } else {
    //    //                                                        strFailed_Format = strFailed_Format + String.valueOf(sRecordNo) + ", ";
    //    //                                                        failed = failed + 1;
    //    //                                                    }
    //                                                    } else {
    //                                                        strFailed_Empty = strFailed_Empty + String.valueOf(sRecordNo) + ", ";
    //                                                        failed = failed + 1;
    //                                                    }
    //                                                }
    //                                                CommonFunction.LogDebugString(log, "Performance", "END");
    //                                                Connection[] temp_connection = new Connection[]{conns};
    //                                                dbNew.CloseDatabase(temp_connection);
    //                                                // || !"".equals(strFailed_Format)
    //                                                if (!"".equals(strFailedAgency) || !"".equals(strFailed_SOPINEncript) || !"".equals(strFailed_Empty)) {
    //                                                    String sSum="";
    //                                                    if(!"".equals(strFailedAgency)){
    //                                                        sSum = "- The Agency Is Invalid - Line Number: " + strFailedAgency + "\n";
    //                                                    }
    //    //                                                if(!"".equals(strFailed_Format)) {
    //    //                                                    sSum = "- Incorrect format of TOKEN_SN: - Line Number: " + strFailed_Format + "\n";
    //    //                                                }
    //                                                    if(!"".equals(strFailed_SOPINEncript)) {
    //                                                        sSum = "- TOKEN_SOPIN encoding is not correct - Line Number: " + strFailed_SOPINEncript + "\n";
    //                                                    }
    //                                                    if(!"".equals(strFailed_Empty)) {
    //                                                        sSum = "- Token information cannot be empty - Line Number: " + strFailed_Empty;
    //                                                    }
    //                                                    sessionsa.setAttribute("sessTokenImportFailed", sSum);
    //    //                                                CommonFunction.LogDebugString(log, "STR_VIEW-1", sSum);
    //                                                }
    //                                                //+strFailed_Format
    //                                                strView = "0###" + String.valueOf(success) + "###" + String.valueOf(failed)
    //                                                        + "###" + String.valueOf(updated) + "###" + strFailedAgency+strFailed_SOPINEncript+strFailed_Empty;
    //    //                                            CommonFunction.LogDebugString(log, "STR_VIEW-2", strView);
    //                                            } else {
    //                                                strView = "1###" + sValueFailColumnName;
    //                                            }
    //                                            //</editor-fold>
    //                                        }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                            CommonFunction.LogErrorServlet(log, "TokenImport_Java: User login does not exist.");
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                        CommonFunction.LogErrorServlet(log, "TokenImport_Java: Invalid agency.");
                                    }
                                }
                            } else {
                                strView = "1#" + Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_FORMAT;
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
    
    //<editor-fold defaultstate="collapsed" desc="S_BO_TOKEN_IMPORT_INNER">
    public int S_BO_TOKEN_IMPORT_INNER(Connection conns, CallableStatement proc_stmt, String pTOKEN_SN, String pTOKEN_SOPIN,
        String pTOKEN_VERSION, String pBRANCH_ID, String pUSER_BY) throws Exception {
        int convrtr = 0;
        try {
            proc_stmt = conns.prepareCall("{ call S_BO_TOKEN_IMPORT(?,?,?,?,?,?) }");
            proc_stmt.setString(1, pTOKEN_SN);
            proc_stmt.setString(2, pTOKEN_SOPIN);
            proc_stmt.setInt(3, Integer.parseInt(pTOKEN_VERSION));
            proc_stmt.setInt(4, Integer.parseInt(pBRANCH_ID));
            proc_stmt.setString(5, pUSER_BY);
            proc_stmt.registerOutParameter(6, java.sql.Types.INTEGER);
            proc_stmt.execute();
            convrtr = proc_stmt.getInt(6);
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
        }
        return convrtr;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="S_BO_TOKEN_IMPORT_MULTIPLE">
    public int S_BO_TOKEN_IMPORT_MULTIPLE(Connection conns, CallableStatement proc_stmt, String pTOKEN_SN, String pTOKEN_SOPIN,
        int pTOKEN_VERSION, int pBRANCH_ID, int pUSER_BY) throws Exception {
        int convrtr = 0;
        try {
            proc_stmt = conns.prepareCall("{ call S_BO_TOKEN_IMPORT_MULTIPLE(?,?,?,?,?) }");
            proc_stmt.setString(1, pTOKEN_SN);
            proc_stmt.setString(2, pTOKEN_SOPIN);
            proc_stmt.setInt(3, pTOKEN_VERSION);
            proc_stmt.setInt(4, pBRANCH_ID);
            proc_stmt.setInt(5, pUSER_BY);
            proc_stmt.execute();
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
        }
        return convrtr;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="S_BO_TOKEN_IMPORT_UPDATE_INNER">
    public int S_BO_TOKEN_IMPORT_UPDATE_INNER(Connection conns, CallableStatement proc_stmt, String pTOKEN_SN, String pTOKEN_SOPIN, int pTOKEN_VERSION,
            int pBRANCH_ID, String pUSER_BY) throws Exception {
        int convrtr = 0;
        try {
            proc_stmt = conns.prepareCall("{ call S_BO_TOKEN_IMPORT_UPDATE(?,?,?,?,?,?) }");
            proc_stmt.setString(1, pTOKEN_SN);
            proc_stmt.setString(2, pTOKEN_SOPIN);
            proc_stmt.setInt(3, pTOKEN_VERSION);
            proc_stmt.setInt(4, pBRANCH_ID);
            proc_stmt.setString(5, pUSER_BY);
            proc_stmt.registerOutParameter(6, java.sql.Types.INTEGER);
//            CommonFunction.LogDebugString(log, "S_BO_TOKEN_IMPORT_UPDATE", proc_stmt.toString());
            proc_stmt.execute();
            convrtr = proc_stmt.getInt(6);
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
        }
        return convrtr;
    }
    //</editor-fold>

    private static ArrayList readExcelFile(InputStream myInput) {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try {
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                ArrayList cellStoreArrayList = new ArrayList();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    cellStoreArrayList.add(myCell);
                }
                cellArrayLisstHolder.add(cellStoreArrayList);
            }
        } catch (Exception e) {
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return cellArrayLisstHolder;
    }

    private static ArrayList readExcelFileXLSX(InputStream myInput) {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try {
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                XSSFRow myRow = (XSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                ArrayList cellStoreArrayList = new ArrayList();
                while (cellIter.hasNext()) {
                    XSSFCell myCell = (XSSFCell) cellIter.next();
                    cellStoreArrayList.add(myCell);
                }
                cellArrayLisstHolder.add(cellStoreArrayList);
            }
        } catch (Exception e) {
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
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
