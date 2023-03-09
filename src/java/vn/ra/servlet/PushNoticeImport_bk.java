/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

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
import vn.ra.object.ATTRIBUTE_DATA;
import vn.ra.object.ATTRIBUTE_VALUES;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.PUSH_TOKEN;
import vn.ra.object.PUSH_TOKEN_EDITED;
import vn.ra.object.TOKEN;
import vn.ra.process.CommonFunction;
import vn.ra.process.CommonReferServlet;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
public class PushNoticeImport_bk extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PushNoticeImport_bk.class.getName());

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
                    String sessLanguage = sessionsa.getAttribute("sessVN").toString();
                    String USER_LOG = EscapeUtils.CheckTextNull(sessionsa.getAttribute("sUserID").toString().trim());
//                    String sCOLLECT_ENABLED = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SESS_COLLECT_ENABLED").toString().trim());
                    String sCONTENT_PUSHNOTICE = EscapeUtils.CheckTextNull(sessionsa.getAttribute("CONTENT_PUSHNOTICE").toString().trim());
                    String sLINK_PUSHNOTICE = EscapeUtils.CheckTextNull(sessionsa.getAttribute("LINK_PUSHNOTICE").toString().trim());
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
                                    String sColumnTokenSTT = "No";//conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_PUSH_STT);
                                    String sColumnTokenMST = "TAX_CODE";//conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_PUSH_MST);
                                    String sColumnTokenMNS = "BUDGET_CODE";//conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_PUSH_MNS);
                                    String sColumnTokenDecision = "DECISION_NUMBER";//conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_PUSH_CMND);
                                    String sColumnTokenCMND = "IDENTITY_CARD";//conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_PUSH_CMND);
                                    String sColumnTokenHC = "PASSPORT";// conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_PUSH_HC);
                                    String sColumnCCCD = "CITIZEN_IDENTITY";
                                    String sColumnCertSN = "CERTIFICATION_SN";
                                    int success = 0;
                                    int failed = 0;
                                    String strFailedNoRecord = "";
                                    String strFailedNoToken = "";
//                                    String strFailedNoNumber = "";
                                    ArrayList dataHolder;
                                    ArrayList cellStoreArrayList;
                                    int indexOfSTT = 100;
                                    int indexOfMST = 100;
                                    int indexOfMNS = 100;
                                    int indexOfDECISION = 100;
                                    int indexOfCMND = 100;
                                    int indexOfHC = 100;
                                    int indexOfCCCD = 100;
                                    int indexOfSN = 100;
                                    if (isXLSX > 0) {
                                        dataHolder = CommonFunction.readExcelImportPushXLSX(fileUploaded);
                                    } else {
                                        dataHolder = CommonFunction.readExcelImportPushXLS(fileUploaded);
                                    }
                                    //<editor-fold defaultstate="collapsed" desc="Import Process">
                                    cellStoreArrayList = (ArrayList) dataHolder.get(0);
                                    for (int i = 0; i < cellStoreArrayList.size(); i++) {
                                        if (sColumnTokenSTT.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfSTT = i;
                                        }
                                        if (sColumnTokenMST.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfMST = i;
                                        }
                                        if (sColumnTokenMNS.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfMNS = i;
                                        }
                                        if (sColumnTokenDecision.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfDECISION = i;
                                        }
                                        if (sColumnTokenCMND.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfCMND = i;
                                        }
                                        if (sColumnTokenHC.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfHC = i;
                                        }
                                        if (sColumnCCCD.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfCCCD = i;
                                        }
                                        if (sColumnCertSN.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfSN = i;
                                        }
                                    }
                                    boolean booFailColumnName = true;
                                    String sValueFailColumnName = "OK";
                                    if (indexOfSTT == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_FORMAT_COLUMN_INVALID;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfMST == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_FORMAT_COLUMN_INVALID;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfMNS == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_FORMAT_COLUMN_INVALID;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfDECISION == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_FORMAT_COLUMN_INVALID;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfCMND == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_FORMAT_COLUMN_INVALID;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfHC == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_FORMAT_COLUMN_INVALID;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfCCCD == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_FORMAT_COLUMN_INVALID;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfSN == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_FORMAT_COLUMN_INVALID;
                                        booFailColumnName = false;
                                    }
                                    if (booFailColumnName == true) {
                                        for (int i = 1; i < dataHolder.size(); i++) {
                                            cellStoreArrayList = (ArrayList) dataHolder.get(i);
                                            String strSTT;
                                            String strMST;
                                            String strMNS;
                                            String strDECISION;
                                            String strCMND;
                                            String strHC;
                                            String strCCCD;
                                            String strSN;
                                            strSTT = cellStoreArrayList.get(indexOfSTT).toString();
                                            strSTT = strSTT.trim().replace(".0", "");
                                            strMST = cellStoreArrayList.get(indexOfMST).toString();
                                            strMNS = cellStoreArrayList.get(indexOfMNS).toString();
                                            strDECISION = cellStoreArrayList.get(indexOfDECISION).toString();
                                            strCMND = cellStoreArrayList.get(indexOfCMND).toString();
                                            strHC = cellStoreArrayList.get(indexOfHC).toString();
                                            strCCCD = cellStoreArrayList.get(indexOfCCCD).toString();
                                            strSN = cellStoreArrayList.get(indexOfSN).toString();
                                            if (!"".equals(strMST) || !"".equals(strMNS) || !"".equals(strDECISION) || !"".equals(strCMND)
                                                || !"".equals(strHC) || !"".equals(strCCCD) || !"".equals(strSN))
                                            {
                                                //<editor-fold defaultstate="collapsed" desc="### Process OLD">
                                                /*String sParam = db.S_BO_CERTIFICATION_UPDATE_PUSH_NOTIFICATION_COLLECTING(strMST, strMNS, strCMND, strHC,
                                                    sCOLLECT_ENABLED, USER_LOG);
                                                if("0".equals(sParam))
                                                {
                                                    success = success + 1;
                                                }
                                                else {
                                                    strFailedNoToken = strFailedNoToken + strSTT + ", ";
                                                    failed = failed + 1;
                                                }*/
                                                //</editor-fold>
                                                
                                                //<editor-fold defaultstate="collapsed" desc="Process NEW">
                                                TOKEN[][] rsToken = new TOKEN[1][];
                                                int[] inResult = new int[1];
                                                String[] sUIDResult = new String[2];
                                                CommonReferServlet.collectFieldToUID(strMST, strMNS, strDECISION, strCMND, strHC, strCCCD, sUIDResult);
                                                String sEnterpriseCert = sUIDResult[0];
                                                String sPersonalCert = sUIDResult[1];
                                                db.S_BO_CERTIFICATION_GET_TOKEN_ID(strMST, strMNS, strCMND, strHC, strCCCD,
                                                    strSN, rsToken, inResult, strDECISION, sEnterpriseCert, sPersonalCert);
                                                if(inResult[0] == 0 && rsToken[0].length > 0)
                                                {
                                                    String sJSON_PUSH_NEW="";
                                                    String sJSON_PUSH_OLD = EscapeUtils.CheckTextNull(rsToken[0][0].PUSH_NOTICE_JSON);
                                                    if(!"".equals(sJSON_PUSH_OLD))
                                                    {
                                                        ObjectMapper objectMapper = new ObjectMapper();
                                                        PUSH_TOKEN jsonParse = objectMapper.readValue(sJSON_PUSH_OLD, PUSH_TOKEN.class);
                                                        if(!"".equals(sCONTENT_PUSHNOTICE))
                                                        {
                                                            jsonParse.PUSH_NOTICE_TEXT = sCONTENT_PUSHNOTICE;
                                                            jsonParse.PUSH_NOTICE_LINK = sLINK_PUSHNOTICE;
                                                            sJSON_PUSH_NEW = objectMapper.writeValueAsString(jsonParse);
                                                        } else {
                                                            PUSH_TOKEN itemPushNoContent = new PUSH_TOKEN();
                                                            itemPushNoContent.PUSH_NOTICE_BGR_COLOR = jsonParse.PUSH_NOTICE_BGR_COLOR;
                                                            itemPushNoContent.PUSH_NOTICE_CONTENT = jsonParse.PUSH_NOTICE_CONTENT;
                                                            itemPushNoContent.PUSH_NOTICE_TEXT_COLOR = jsonParse.PUSH_NOTICE_TEXT_COLOR;
                                                            itemPushNoContent.PUSH_NOTICE_URL = jsonParse.PUSH_NOTICE_URL;
                                                            sJSON_PUSH_NEW = objectMapper.writeValueAsString(itemPushNoContent);
                                                        }
//                                                        CommonFunction.LogDebugString(log, "sJSON_PUSH_NEW", sJSON_PUSH_NEW);
//                                                        CommonFunction.LogDebugString(log, "TOKEN_ID", String.valueOf(rsToken[0][0].ID));
                                                        db.S_BO_TOKEN_UPDATE_PUSH_NOTICE_JSON(String.valueOf(rsToken[0][0].ID), sJSON_PUSH_NEW, USER_LOG);
                                                        success = success + 1;
                                                    } else {
                                                        if(!"".equals(sCONTENT_PUSHNOTICE))
                                                        {
                                                            GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
                                                            db.S_BO_GENERAL_POLICY_LIST(sessLanguage,rsPolicy);
                                                            if (rsPolicy[0].length > 0)
                                                            {
                                                                for(GENERAL_POLICY rsPolicy1: rsPolicy[0])
                                                                {
                                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_PUSH_NOTICE_JSON))
                                                                    {
                                                                        ObjectMapper oMapperDefaultParse = new ObjectMapper();
                                                                        String sValueDefaultJSON = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
//                                                                        CommonFunction.LogDebugString(log, "sValueDefaultJSON", sValueDefaultJSON);
                                                                        if(!"".equals(sValueDefaultJSON))
                                                                        {
                                                                            String strPUSH_NOTICE_CONTENT = "";
                                                                            String strPUSH_NOTICE_URL = "";
                                                                            String strNOTICE_BGR_COLOR = "";
                                                                            String strNOTICE_TEXT_COLOR = "";
                                                                            PUSH_TOKEN_EDITED itemParsePush = oMapperDefaultParse.readValue(sValueDefaultJSON, PUSH_TOKEN_EDITED.class);
                                                                            for (PUSH_TOKEN_EDITED.Attribute attribute : itemParsePush.getAttributes()) {
                                                                                if(EscapeUtils.CheckTextNull(attribute.getName()).equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_CONTENT))
                                                                                {
                                                                                    strPUSH_NOTICE_CONTENT = EscapeUtils.CheckTextNull(attribute.getValue());
                                                                                }
                                                                                if(EscapeUtils.CheckTextNull(attribute.getName()).equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_URL))
                                                                                {
                                                                                    strPUSH_NOTICE_URL = EscapeUtils.CheckTextNull(attribute.getValue());
                                                                                }
                                                                                if(EscapeUtils.CheckTextNull(attribute.getName()).equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_BGR_COLOR))
                                                                                {
                                                                                    strNOTICE_BGR_COLOR = EscapeUtils.CheckTextNull(attribute.getValue());
                                                                                }
                                                                                if(EscapeUtils.CheckTextNull(attribute.getName()).equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_TEXT_COLOR))
                                                                                {
                                                                                    strNOTICE_TEXT_COLOR = EscapeUtils.CheckTextNull(attribute.getValue());
                                                                                }
                                                                            }
                                                                            PUSH_TOKEN itemPushNoContent = new PUSH_TOKEN();
                                                                            itemPushNoContent.PUSH_NOTICE_BGR_COLOR = strNOTICE_BGR_COLOR;
                                                                            itemPushNoContent.PUSH_NOTICE_CONTENT = strPUSH_NOTICE_CONTENT;
                                                                            itemPushNoContent.PUSH_NOTICE_TEXT_COLOR = strNOTICE_TEXT_COLOR;
                                                                            itemPushNoContent.PUSH_NOTICE_URL = strPUSH_NOTICE_URL;
                                                                            itemPushNoContent.PUSH_NOTICE_TEXT = sCONTENT_PUSHNOTICE;
                                                                            itemPushNoContent.PUSH_NOTICE_LINK = sLINK_PUSHNOTICE;
                                                                            sJSON_PUSH_NEW = oMapperDefaultParse.writeValueAsString(itemPushNoContent);
                                                                        }
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
//                                                        CommonFunction.LogDebugString(log, "sJSON_PUSH_NEW", sJSON_PUSH_NEW);
//                                                        CommonFunction.LogDebugString(log, "TOKEN_ID", String.valueOf(rsToken[0][0].ID));
                                                        db.S_BO_TOKEN_UPDATE_PUSH_NOTICE_JSON(String.valueOf(rsToken[0][0].ID), sJSON_PUSH_NEW, USER_LOG);
                                                        success = success + 1;
                                                    }
                                                } else {
                                                    // not found token
                                                    strFailedNoToken = strFailedNoToken + strSTT + ", ";
                                                    failed = failed + 1;
                                                }
                                                //</editor-fold>
                                                
                                            } else {
                                                // No Data in record
                                                strFailedNoRecord = strFailedNoRecord + strSTT + ", ";
                                                failed = failed + 1;
                                            }
                                        }
                                        String sSum="";
                                        if (!"".equals(strFailedNoToken) || !"".equals(strFailedNoRecord))
                                        {
                                            if(!"".equals(strFailedNoToken)) {
                                                sSum = "Error - No data found - STT: " + strFailedNoToken + "\n";
                                            }
                                            if(!"".equals(strFailedNoRecord)) {
                                                sSum = "Error - Empty record - STT: " + strFailedNoRecord;
                                            }
//                                            if(!"".equals(strFailedNoNumber)) {
//                                                sSum = "Wrong number format: " + strFailedNoNumber;
//                                            }
                                            sessionsa.setAttribute("sessTokenImportFailed", sSum);
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
