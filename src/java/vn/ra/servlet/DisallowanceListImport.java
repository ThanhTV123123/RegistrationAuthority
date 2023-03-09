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
import vn.ra.object.DisallowanceList;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.PUSH_TOKEN;
import vn.ra.object.PUSH_TOKEN_EDITED;
import vn.ra.object.TOKEN;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
public class DisallowanceListImport extends HttpServlet {
private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DisallowanceListImport.class.getName());
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
//                    String sessLanguage = sessionsa.getAttribute("sessVN").toString();
                    String USER_ID_LOG = EscapeUtils.CheckTextNull(sessionsa.getAttribute("UserID").toString().trim());
//                    String sSESS_DISALLOWANCE_LIST = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SESS_DISALLOWANCE_LIST").toString().trim());
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
                                    Config conf = new Config();
                                    String sColumnTokenSTT = conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_PUSH_STT);
                                    String sColumnTokenPhone = conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_DISALLOWANCE_PHONE);
                                    String sColumnTokenEmail = conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_DISALLOWANCE_EMAIL);
                                    int success = 0;
                                    int failed = 0;
                                    String strFailedNoRecord = "";
                                    ArrayList dataHolder;
                                    ArrayList cellStoreArrayList;
                                    int indexOfSTT = 100;
                                    int indexOfPhone = 100;
                                    int indexOfEmail = 100;
                                    if (isXLSX > 0) {
                                        dataHolder = CommonFunction.readExcelImportDisallowanceXLSX(fileUploaded);
                                    } else {
                                        dataHolder = CommonFunction.readExcelImportDisallowanceXLS(fileUploaded);
                                    }
                                    //<editor-fold defaultstate="collapsed" desc="Import Process">
                                    cellStoreArrayList = (ArrayList) dataHolder.get(0);
                                    for (int i = 0; i < cellStoreArrayList.size(); i++) {
                                        if (sColumnTokenSTT.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfSTT = i;
                                        }
                                        if (sColumnTokenPhone.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfPhone = i;
                                        }
                                        if (sColumnTokenEmail.equals(cellStoreArrayList.get(i).toString().trim())) {
                                            indexOfEmail = i;
                                        }
                                    }
                                    boolean booFailColumnName = true;
                                    String sValueFailColumnName = "OK";
                                    if (indexOfSTT == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_PUSH_STT;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfPhone == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_DISALLOWANCE_PHONE;
                                        booFailColumnName = false;
                                    }
                                    if (indexOfEmail == 100) {
                                        sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_DISALLOWANCE_EMAIL;
                                        booFailColumnName = false;
                                    }
                                    if (booFailColumnName == true)
                                    {
                                        for (int i = 1; i < dataHolder.size(); i++) {
                                            cellStoreArrayList = (ArrayList) dataHolder.get(i);
                                            String strSTT;
                                            String strPhone;
                                            String strEmail;
                                            strSTT = cellStoreArrayList.get(indexOfSTT).toString();
                                            strSTT = strSTT.trim().replace("'", "");
                                            strSTT = strSTT.trim().replace(".0", "");
                                            strPhone = cellStoreArrayList.get(indexOfPhone).toString();
                                            strPhone = strPhone.trim().replace("'", "");
                                            strEmail = cellStoreArrayList.get(indexOfEmail).toString();
                                            strEmail = strEmail.trim().replace("'", "");
                                            if (!"".equals(strPhone) || !"".equals(strEmail))
                                            {
                                                db.S_BO_CERTIFICATION_ATTRIBUTE_UPDATE_BLACK_LIST(strEmail, strPhone, Integer.parseInt(USER_ID_LOG));
                                                success = success + 1;
                                            } else {
                                                strFailedNoRecord = strFailedNoRecord + strSTT + ", ";
                                                failed = failed + 1;
                                            }
                                        }
                                        String sSum = "";
                                        if(failed != 0) {
                                            sSum = "Error - Empty record - STT: " + strFailedNoRecord;
                                        }
                                        if(!"".equals(sSum))
                                        {
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
