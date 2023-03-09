/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
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
import vn.ra.object.ATTRIBUTE_VALUES;
import vn.ra.object.MENULINK_TOKEN;
import vn.ra.object.PUSH_TOKEN;
import vn.ra.object.TOKEN;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author USER
 */
public class TokenActionImport extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TokenActionImport.class.getName());

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
//            CommonFunction com = new CommonFunction();
            String fileUploaded = "";
            String fileName;
//            String fileMimeType;
//            String pIS_ACTION = "";
//            String pLOCK_REASON = "";
            String strView = "";
            int isXLSX = 0;
//            InputStream thanhtv = null;
            boolean strAlert = false;
            boolean bCheckfile = false;
            HttpSession sessionsa = request.getSession(false);
//            String sessLanguage = sessionsa.getAttribute("sessVN").toString();
            String loginFullname = request.getSession(false).getAttribute("sesFullname").toString().trim();
            String sUID_Update = sessionsa.getAttribute("sUserID").toString().trim();
            ConnectDatabase db = new ConnectDatabase();
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
                    String sessAllApplyEnabled = request.getSession(false).getAttribute("sessAllApplyEnabled").toString().trim();
                    String sessACTION_TOKEN = request.getSession(false).getAttribute("sessACTION_TOKEN").toString().trim();
                    String pLOCK_REASON = request.getSession(false).getAttribute("pLOCK_REASON").toString().trim();
                    String sessCOLOR_TEXT = request.getSession(false).getAttribute("sessCOLOR_TEXT").toString().trim();
                    String sessCOLOR_BKGR = request.getSession(false).getAttribute("sessCOLOR_BKGR").toString().trim();
                    String sessLINK_NOTICE = request.getSession(false).getAttribute("sessLINK_NOTICE").toString().trim();
                    String sessNOTICE_INFO = request.getSession(false).getAttribute("sessNOTICE_INFO").toString().trim();
                    String sessNAME_LINK = request.getSession(false).getAttribute("sessNAME_LINK").toString().trim();
                    String sessLINK_VALUE = request.getSession(false).getAttribute("sessLINK_VALUE").toString().trim();
                    String vIS_PUSH_NOTICE_SET_NO = request.getSession(false).getAttribute("sessPushDefaultEnabled").toString().trim();
                    String vIS_MENU_LINK_SET_NO = request.getSession(false).getAttribute("sessMenuDefaultEnabled").toString().trim();
                    request.getSession(false).setAttribute("sessACTION_TOKEN", null);
                    request.getSession(false).setAttribute("pLOCK_REASON", null);
                    request.getSession(false).setAttribute("sessCOLOR_TEXT", null);
                    request.getSession(false).setAttribute("sessCOLOR_BKGR", null);
                    request.getSession(false).setAttribute("sessLINK_NOTICE", null);
                    request.getSession(false).setAttribute("sessNOTICE_INFO", null);
                    request.getSession(false).setAttribute("sessNAME_LINK", null);
                    request.getSession(false).setAttribute("sessLINK_VALUE", null);
                    request.getSession(false).setAttribute("sessAllApplyEnabled", null);
                    request.getSession(false).setAttribute("sessPushDefaultEnabled", null);
                    request.getSession(false).setAttribute("sessMenuDefaultEnabled", null);
                    if("0".equals(sessAllApplyEnabled)) {
                        String contentType = request.getContentType();
                        if ((contentType != null) && (contentType.contains("multipart/form-data"))) {
                            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                            if (isMultipart) {
                                FileItemFactory factory = new DiskFileItemFactory();
                                ServletFileUpload upload = new ServletFileUpload(factory);
                                List items = upload.parseRequest(request);
                                Iterator iterator = items.iterator();
                                while (iterator.hasNext()) {
                                    FileItem item = (FileItem) iterator.next();
                                    if (!item.isFormField()) {
                                        fileName = item.getName();
                                        strAlert = CommonFunction.checkFileSpecial(fileName);
                                        if (strAlert == true) {
                                            String sExtendFile = CommonFunction.getExtendFile(fileName);
                                            fileName = fileName.replace("." + sExtendFile, "") + "_" + CommonFunction.generateNumberDays() + "." + sExtendFile;
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
                                    } else {
//                                        if ("pIS_ACTION".equals(item.getFieldName())) {
//                                            pIS_ACTION = item.getString();
//                                        }
    //                                    if ("pLOCK_REASON".equals(item.getFieldName())) {
    //                                        pLOCK_REASON = EscapeUtils.CheckTextNull(item.getString());
    //                                    }
                                    }
                                }
                            }
                            if (strAlert == false) {
                                strView = "1#" + Definitions.CONFIG_EXCEPTION_STRING_ERROR_SPECIAL;
                            } else {
                                if (bCheckfile == true) {
                                    String sColumnTokenSTT = "No";
                                    String sColumnTokenSN = "TOKEN_SN";
                                    String sColumnMST = "TAX_CODE";
                                    int success = 0;
                                    int failed = 0;
                                    int failed_existrequest = 0;
                                    int failed_noexists = 0;
                                    int failed_norecord = 0;
                                    String strFailedNoExist = "";
                                    String strFailedExistRequest = "";
                                    String strFailedNoRecord = "";
                                    ArrayList dataHolder;
                                    ArrayList cellStoreArrayList;
                                    int indexOfSTT = 100;
                                    int indexOfTokenSN = 100;
                                    int indexOfMST = 100;
                                    if (isXLSX > 0) {
                                        dataHolder = CommonFunction.readExcelImportTokenActionXLSX(fileUploaded);
                                    } else {
                                        dataHolder = CommonFunction.readExcelImportTokenActionXLS(fileUploaded);
                                    }
                                    //<editor-fold defaultstate="collapsed" desc="### PROCESS">
                                    cellStoreArrayList = (ArrayList) dataHolder.get(0);
                                    for (int i = 0; i < cellStoreArrayList.size(); i++) {
                                        if (sColumnTokenSTT.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfSTT = i;
                                        }
                                        if (sColumnTokenSN.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfTokenSN = i;
                                        }
                                        if (sColumnMST.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfMST = i;
                                        }
                                    }
                                    boolean booFailColumnName = true;
                                    String sValueFailColumnName = "OK";
                                    if (indexOfSTT == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_STT;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfTokenSN == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_DISALLOWANCE_TOKENSN;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfMST == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_DISALLOWANCE_MST;
                                        booFailColumnName = false;
                                    }
                                    if (booFailColumnName == true)
                                    {
                                        String sTypeName;
                                        int intTypeID = 0;
                                        int intTOKEN_ATTR_STATE = 0;
                                        if(sessACTION_TOKEN.equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_LOCK))
                                        {
                                            intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                            sTypeName = Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_LOCK;
                                            intTypeID = Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOCK;
                                        } else if(sessACTION_TOKEN.equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_PUSH_NOTFICATION))
                                        {
                                            intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_COMMITED);
                                            sTypeName = Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_PUSH_NOTFICATION;
                                            intTypeID = Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PUSH_NOTFICATION;
                                        } else if(sessACTION_TOKEN.equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_MENU_LINK))
                                        {
                                            intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_COMMITED);
                                            sTypeName = Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_MENU_LINK;
                                            intTypeID = Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_MENU_LINK;
                                        } else {
                                            intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                            sTypeName = Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_UNLOCK;
                                            intTypeID = Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_UNLOCK;
                                        }
                                        for (int i = 1; i < dataHolder.size(); i++)
                                        {
                                            cellStoreArrayList = (ArrayList) dataHolder.get(i);
                                            String strSTT;
                                            String strTokenSN;
                                            String strMST;
                                            strSTT = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfSTT).toString(), true);
                                            strTokenSN = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfTokenSN).toString(), false);
                                            strMST = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfMST).toString(), false);
                                            if (!"".equals(strTokenSN) || !"".equals(strMST))
                                            {
                                                String sToken_ID = "";
                                                TOKEN[][] resToken = new TOKEN[1][];
                                                if("".equals(strTokenSN))
                                                {
                                                    strMST = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE + ":" + strMST;
                                                    db.S_BO_TOKEN_GET_BY_ENTERPRISE_ID(strMST, resToken);
                                                } else {
                                                    db.S_BO_TOKEN_GET_BY_TOKEN_SN(strTokenSN, resToken);
                                                }
                                                if(resToken[0].length > 0)
                                                {
                                                    strTokenSN = resToken[0][0].TOKEN_SN;
                                                    sToken_ID = String.valueOf(resToken[0][0].ID);
                                                    ATTRIBUTE_VALUES valueATTR = new ATTRIBUTE_VALUES();
                                                    valueATTR.setTokenSn(strTokenSN);
                                                    valueATTR.setTypeName(sTypeName);
                                                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                    if(sTypeName.equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_LOCK))
                                                    {
                                                        valueATTR.setActionReason(pLOCK_REASON);
                                                    }
                                                    valueATTR.setCreateUser(loginFullname + " (" + sUID_Update + ")");
                                                    valueATTR.setCreateDt(new Date());
                                                    valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                    valueATTR.setApproveDt(new Date());
                                                    String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                    String sParam = db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sToken_ID), intTypeID,
                                                        intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                    if("0".equals(sParam))
                                                    {
                                                        if(sessACTION_TOKEN.equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_PUSH_NOTFICATION))
                                                        {
                                                            String strReqPush = Definitions.CONFIG_UPDATE_DEFAULT_VALUE_NULL;
                                                            if (!"1".equals(vIS_PUSH_NOTICE_SET_NO)) {
                                                                ObjectMapper objectMapper = new ObjectMapper();
                                                                PUSH_TOKEN itemParsePush = new PUSH_TOKEN();
                                                                itemParsePush.PUSH_NOTICE_CONTENT = sessNOTICE_INFO;
                                                                itemParsePush.PUSH_NOTICE_URL = sessLINK_NOTICE;
                                                                itemParsePush.PUSH_NOTICE_TEXT_COLOR = sessCOLOR_TEXT;
                                                                itemParsePush.PUSH_NOTICE_BGR_COLOR = sessCOLOR_BKGR;
                                                                strReqPush = objectMapper.writeValueAsString(itemParsePush);
                                                            }
                                                            db.S_BO_TOKEN_UPDATE(Integer.parseInt(sToken_ID), "",
                                                                "", strReqPush,"", "", sUID_Update);
                                                        } else if(sessACTION_TOKEN.equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_MENU_LINK))
                                                        {
                                                            String strReqMenu = Definitions.CONFIG_UPDATE_DEFAULT_VALUE_NULL;
                                                            if (!"1".equals(vIS_MENU_LINK_SET_NO)) {
                                                                ObjectMapper objectMapper = new ObjectMapper();
                                                                MENULINK_TOKEN itemParseMenu = new MENULINK_TOKEN();
                                                                itemParseMenu.MENU_LINK_NAME = sessNAME_LINK;
                                                                itemParseMenu.MENU_LINK_URL = sessLINK_VALUE;
                                                                strReqMenu = objectMapper.writeValueAsString(itemParseMenu);
                                                            }
                                                            db.S_BO_TOKEN_UPDATE(Integer.parseInt(sToken_ID), "",
                                                                strReqMenu, "", "", "", sUID_Update);
                                                        }
                                                        success = success+1;
                                                    } else {
                                                        failed_existrequest = failed_existrequest + 1;
                                                        strFailedExistRequest = strFailedExistRequest + strSTT + ", ";
                                                    }
                                                } else {
                                                    strFailedNoExist = strFailedNoExist + strSTT + ", ";
                                                    failed_noexists = failed_noexists + 1;
                                                }
                                            } else {
                                                strFailedNoRecord = strFailedNoRecord + strSTT + ", ";
                                                failed_norecord = failed_norecord + 1;
                                            }
                                        }
                                        String sSum = "";
                                        if(failed_noexists != 0 || failed_norecord != 0 || failed_existrequest != 0) {
                                            if(!"".equals(strFailedExistRequest)) {
                                                sSum = "Error - Already exists the token request - STT: " + strFailedExistRequest + "\n";
                                            }
                                            if(!"".equals(strFailedNoExist)) {
                                                sSum = sSum + "Error - No data found in the system - STT: " + strFailedNoExist + "\n";
                                            }
                                            if(!"".equals(strFailedNoRecord)) {
                                                sSum = sSum + "Error - Empty record - STT: " + strFailedNoRecord;
                                            }
                                            failed = failed_noexists + failed_norecord + failed_existrequest;
                                        } else {
                                            failed = 0;
                                        }
                                        if(!"".equals(sSum))
                                        {
                                            sessionsa.setAttribute("sessTokenActionImportFailed", sSum);
                                        }
                                        strView = "0###" + String.valueOf(success) + "###" + String.valueOf(failed) + "###" + sSum;
                                    } else {
                                        strView = "1###" + sValueFailColumnName;
                                    }
                                    //</editor-fold>
                                } else {
                                    strView = "1#" + Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_FORMAT;
                                }
                            }
                        }
                    } else if("1".equals(sessAllApplyEnabled)) {
                        if(sessACTION_TOKEN.equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_PUSH_NOTFICATION))
                        {
                            String strReqPush = Definitions.CONFIG_UPDATE_DEFAULT_VALUE_NULL;
                            if (!"1".equals(vIS_PUSH_NOTICE_SET_NO)) {
                                ObjectMapper objectMapper = new ObjectMapper();
                                PUSH_TOKEN itemParsePush = new PUSH_TOKEN();
                                itemParsePush.PUSH_NOTICE_CONTENT = sessNOTICE_INFO;
                                itemParsePush.PUSH_NOTICE_URL = sessLINK_NOTICE;
                                itemParsePush.PUSH_NOTICE_TEXT_COLOR = sessCOLOR_TEXT;
                                itemParsePush.PUSH_NOTICE_BGR_COLOR = sessCOLOR_BKGR;
                                strReqPush = objectMapper.writeValueAsString(itemParsePush);
                            }
                            db.S_BO_TOKEN_UPDATE(0, "", "", strReqPush, "", "", sUID_Update);
                            strView = "00###0";
                        } else if(sessACTION_TOKEN.equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_MENU_LINK))
                        {
                            String strReqMenu = Definitions.CONFIG_UPDATE_DEFAULT_VALUE_NULL;
                            if(!"".equals(sessNAME_LINK) || !"".equals(sessLINK_VALUE))
                            {
                                if (!"1".equals(vIS_MENU_LINK_SET_NO)) {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    MENULINK_TOKEN itemParseMenu = new MENULINK_TOKEN();
                                    itemParseMenu.MENU_LINK_NAME = sessNAME_LINK;
                                    itemParseMenu.MENU_LINK_URL = sessLINK_VALUE;
                                    strReqMenu = objectMapper.writeValueAsString(itemParseMenu);
                                }
                            }
                            db.S_BO_TOKEN_UPDATE(0, "", strReqMenu, "", "", "", sUID_Update);
                            strView = "00###0";
                        } else {
                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR+"###0";
                            CommonFunction.LogErrorServlet(log, "TokenActionImport: The request type is not valid when applied to all tokens - " + sessACTION_TOKEN);
                        }
                    } else {
                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR+"###0";
                        CommonFunction.LogErrorServlet(log, "TokenActionImport: The configuration applies to all tokens that are not valid - " + sessAllApplyEnabled);
                    }
                } else if (sOutInner == 2) {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "###0";
                } else {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "###0";
                }
            } catch (NumberFormatException e) {
                log.error(e.getMessage(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR+"###0";
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR+"###0";
            } finally {
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
