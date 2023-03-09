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
 * @author USER
 */
public class CollectProfileImport extends HttpServlet {
    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CollectProfileImport.class.getName());
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
                    String sessControlType = EscapeUtils.CheckTextNull(sessionsa.getAttribute("sessControlType").toString().trim());
                    String sessIdSessIsChoise = EscapeUtils.CheckTextNull(sessionsa.getAttribute("sessIdSessIsChoise").toString().trim());
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
                                    String sColumnTokenSTT = "No";
                                    String sColumnCERTIFICATION_SN = "CERTIFICATION_SN";
//                                    String sColumnPREFIX_UID_ENTERPRISE = "PREFIX_UID_ENTERPRISE";
//                                    String sColumnUID_ENTERPRISE = "UID_ENTERPRISE";
//                                    String sColumnPREFIX_UID_PERSONAL = "PREFIX_UID_PERSONAL";
//                                    String sColumnUID_PERSONAL = "UID_PERSONAL";
                                    int success = 0;
                                    int failed = 0;
                                    String strFailedNoRecord = "";
                                    String strFailedNoToken = "";
                                    ArrayList dataHolder;
                                    ArrayList cellStoreArrayList;
                                    int indexOfSTT = 100;
                                    int indexOfCERTIFICATION_SN = 100;
//                                    int indexOfPREFIX_UID_ENTERPRISE = 100;
//                                    int indexOfUID_ENTERPRISE = 100;
//                                    int indexOfPREFIX_UID_PERSONAL = 100;
//                                    int indexOfUID_PERSONAL = 100;
                                    if (isXLSX > 0) {
                                        dataHolder = CommonFunction.readExcelImportCollectProfileXLSX(fileUploaded);
                                    } else {
                                        dataHolder = CommonFunction.readExcelImportCollectProfileXLS(fileUploaded);
                                    }
                                    //<editor-fold defaultstate="collapsed" desc="Import Process">
                                    cellStoreArrayList = (ArrayList) dataHolder.get(0);
                                    for (int i = 0; i < cellStoreArrayList.size(); i++) {
                                        if (sColumnTokenSTT.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfSTT = i;
                                        }
                                        if (sColumnCERTIFICATION_SN.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfCERTIFICATION_SN = i;
                                        }
//                                        if (sColumnPREFIX_UID_ENTERPRISE.equals(cellStoreArrayList.get(i).toString().trim())) {
//                                            indexOfPREFIX_UID_ENTERPRISE = i;
//                                        }
//                                        if (sColumnUID_ENTERPRISE.equals(cellStoreArrayList.get(i).toString().trim())) {
//                                            indexOfUID_ENTERPRISE = i;
//                                        }
//                                        if (sColumnPREFIX_UID_PERSONAL.equals(cellStoreArrayList.get(i).toString().trim())) {
//                                            indexOfPREFIX_UID_PERSONAL = i;
//                                        }
//                                        if (sColumnUID_PERSONAL.equals(cellStoreArrayList.get(i).toString().trim())) {
//                                            indexOfUID_PERSONAL = i;
//                                        }
                                    }
                                    boolean booFailColumnName = true;
                                    String sValueFailColumnName = "OK";
                                    if (indexOfSTT == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_STT;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfCERTIFICATION_SN == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_MST;
                                        booFailColumnName = false;
                                    }
//                                    if (indexOfPREFIX_UID_ENTERPRISE == 100) {
//                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_MST;
//                                        booFailColumnName = false;
//                                    }
//                                    if (indexOfUID_ENTERPRISE == 100) {
//                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_MNS;
//                                        booFailColumnName = false;
//                                    }
//                                    if (indexOfPREFIX_UID_PERSONAL == 100) {
//                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_MNS;
//                                        booFailColumnName = false;
//                                    }
//                                    if (indexOfUID_PERSONAL == 100) {
//                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_CMND;
//                                        booFailColumnName = false;
//                                    }
                                    if (booFailColumnName == true) {
                                        for (int i = 1; i < dataHolder.size(); i++) {
                                            cellStoreArrayList = (ArrayList) dataHolder.get(i);
                                            String strSTT;
//                                            String strPrefixEnterprise;
                                            String strSN;
//                                            String strUIDEnterprise;
//                                            String strPrefixPersonal;
//                                            String strUIDPersonal;
                                            strSTT = cellStoreArrayList.get(indexOfSTT).toString();
                                            strSTT = strSTT.trim().replace(".0", "");
                                            strSN = cellStoreArrayList.get(indexOfCERTIFICATION_SN).toString();
//                                            strPrefixEnterprise = cellStoreArrayList.get(indexOfPREFIX_UID_ENTERPRISE).toString();
//                                            strUIDEnterprise = cellStoreArrayList.get(indexOfUID_ENTERPRISE).toString();
//                                            strPrefixPersonal = cellStoreArrayList.get(indexOfPREFIX_UID_PERSONAL).toString();
//                                            strUIDPersonal = cellStoreArrayList.get(indexOfUID_PERSONAL).toString();
//                                            if ((!"".equals(strPrefixEnterprise) && !"".equals(strUIDEnterprise)) || (!"".equals(strPrefixPersonal) && !"".equals(strUIDPersonal)))
//                                            {
                                            //<editor-fold defaultstate="collapsed" desc="### Process">
                                            String sEnterpriseCert = "";
//                                            if(!"".equals(strUIDEnterprise)){
//                                                sEnterpriseCert = CommonReferServlet.convertPrefixVNToEN(strPrefixEnterprise + ":" + strUIDEnterprise, true);
//                                            }
                                            String sPersonalCert = "";
//                                            if(!"".equals(strUIDPersonal)){
//                                                sPersonalCert = CommonReferServlet.convertPrefixVNToEN(strPrefixPersonal + ":" + strUIDPersonal, false);
//                                            }
                                            String sParam;
                                            if("1".equals(sessControlType)) {
                                                sParam = db.S_BO_CERTIFICATION_UPDATE_COLLECT_ENABLED("", "", "", "",
                                                    sessIdSessIsChoise, USER_LOG, "", "", sEnterpriseCert, sPersonalCert, strSN);
                                            } else {
                                                sParam = db.S_BO_CERTIFICATION_UPDATE_CROSS_CHECK_ENABLED("", "", "", "",
                                                    sessIdSessIsChoise, USER_LOG, "", "", sEnterpriseCert, sPersonalCert, strSN);
                                            }
                                            if("0".equals(sParam)) {
                                                success = success + 1;
                                            } else {
                                                strFailedNoToken = strFailedNoToken + strSTT + ", ";
                                                failed = failed + 1;
                                            }
                                            //</editor-fold>
//                                            } else {
//                                                // No Data in record
//                                                strFailedNoRecord = strFailedNoRecord + strSTT + ", ";
//                                                failed = failed + 1;
//                                            }
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
