/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import java.io.File;
import java.io.FileInputStream;
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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vn.mobileid.fms.client.JCRConfig;
import vn.mobileid.fms.client.JCRFile;
import vn.mobileid.fms.client.JCRManager;
import vn.ra.object.FILE_PROFILE_DATA;
import vn.ra.process.JackRabbitCommon;

/**
 *
 * @author thanh
 */
public class TestUploadFile extends HttpServlet {
    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TestUploadFile.class.getName());
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
            String fileUploaded = "";
            String fileName = "";
            String fileMimeType = "";
            String sFILE_PROFILE = "";
            JSONArray listJson = new JSONArray();
            InputStream thanhtv = null;
            try {
                String contentType = request.getContentType();
                if ((contentType != null) && (contentType.contains("multipart/form-data"))) {
                    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                    if (isMultipart) {
                        FileItemFactory factory = new DiskFileItemFactory();
                        ServletFileUpload upload = new ServletFileUpload(factory);
                        List items = upload.parseRequest(request);
                        Iterator iterator = items.iterator();
                        ArrayList<FILE_PROFILE_DATA> tempList = new ArrayList<>();
                        while (iterator.hasNext()) {
                            FileItem item = (FileItem) iterator.next();
                            if (!item.isFormField()) {
//                                InputStream thanhtv = null;
                                fileName = item.getName();
                                fileMimeType = getServletContext().getMimeType(FilenameUtils.getName(fileName));
//                                String sExtendFile = getExtendFile(fileName);
//                                fileName = fileName.replace("." + sExtendFile, "") + "_abc." + sExtendFile;
                                String root = getServletContext().getRealPath("/");
                                File path = new File(root + "/uploads");
                                if (!path.exists()) {
                                    boolean status = path.mkdirs();
                                }
                                File uploadedFile = new File(path + "/" + fileName);
                                fileUploaded = path + "/" + fileName;
                                item.write(uploadedFile);
//                                thanhtv = new FileInputStream(fileUploaded);
                                // GET SIZE of FILE
//                                File file = new File(fileUploaded);
//                                double fileLength = 0;
//                                if(file.exists()) {
//                                    fileLength = file.length();
//                                }
                                // ADD LIST FILE
//                                FILE_PROFILE_DATA tempItem = new FILE_PROFILE_DATA();
//                                tempItem.FILE_NAME = fileName;
//                                tempItem.FILE_MIMETYPE = fileMimeType;
//                                tempItem.FILE_SIZE = fileLength;
//                                tempItem.FILE_STREAM = thanhtv;
//                                tempList.add(tempItem);
                            } else {
                                if ("pFILE_PROFILE".equals(item.getFieldName())) {
                                    sFILE_PROFILE = item.getString();
                                }
                            }
                        }
//                        if(tempList.size() > 0)
//                        {
//                            for(int i = 0; i<tempList.size();i++)
//                            {
//                                FILE_PROFILE_DATA rsFILE_PROFILE = new FILE_PROFILE_DATA();
//                                rsFILE_PROFILE.FILE_NAME = CheckTextNull(tempList.get(i).FILE_NAME);
//                                rsFILE_PROFILE.FILE_MIMETYPE = CheckTextNull(tempList.get(i).FILE_MIMETYPE);
//                                rsFILE_PROFILE.FILE_PROFILE = sFILE_PROFILE;
//                                rsFILE_PROFILE.FILE_SIZE = tempList.get(i).FILE_SIZE;
//                                rsFILE_PROFILE.FILE_STREAM = tempList.get(i).FILE_STREAM;
//                                cartToken.AddRoleFunctionsList(rsFILE_PROFILE);
//                            }
//                            request.getSession(false).setAttribute("sessSessionUploadFile", cartToken);
//                        }
                    }
                }
//                int j=1;
//                ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
//                for (FILE_PROFILE_DATA mhIP : ds) {
//                    if(mhIP.FILE_PROFILE.equals(sFILE_PROFILE))
//                    {
//                        JSONObject json = new JSONObject();
//                        json.put("Code", "0");
//                        json.put("Index", j++);
//                        json.put("FILE_NAME", CheckTextNull(mhIP.FILE_NAME));
//                        json.put("FILE_PROFILE", CheckTextNull(mhIP.FILE_PROFILE));
//                        json.put("FILE_SIZE", convertMoneyFromDouble(mhIP.FILE_SIZE / 1024));
//                        listJson.add(json);
//                    }
//                }

                thanhtv = new FileInputStream(fileUploaded);
                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig("http://192.168.2.245:9080/jackrabbit-webapp-2.16.3/server",
                        "RWTRUONGNNT", "RWTRUONGNNT", 128, 10000, "TRUONGNNT", "247_client_");
                JCRFile jrbFile = JackRabbitCommon.getInstance(jcrConfig).uploadFile(fileName, fileMimeType, thanhtv);
                log.info("UUID: " + jrbFile.getUuid() + "; MimeType: " + jrbFile.getMimeType());
                JCRManager.destroyAllJCR();
//                SessionUploadFile cartToken = (SessionUploadFile) request.getSession(false).getAttribute("sessSessionUploadFile");
//                if (cartToken == null) {
//                    cartToken = new SessionUploadFile();
//                }
//                cartToken.AddRoleFunctionsList(rsRole1);
//                request.getSession(false).setAttribute("sessSessionUploadFile", cartToken);
                if (listJson.size() <= 0) {
                    JSONObject json = new JSONObject();
                    json.put("Code", "1");
                    listJson.add(json);
                }
            } catch (NumberFormatException e) {
                log.error(e.getMessage(), e);
                JSONObject json = new JSONObject();
                json.put("Code", "ERROR");
                listJson.add(json);
//                strView = "ERROR###" + e.getMessage();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                JSONObject json = new JSONObject();
                json.put("Code", "ERROR");
                listJson.add(json);
//                strView = "ERROR###" + e.getMessage();
            } finally {
//                if (thanhtv != null) {
//                    thanhtv.close();
//                }
                if ((new File(fileUploaded)).exists()) {
                    (new File(fileUploaded)).delete();
                }
            }
            out.println(listJson);
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
