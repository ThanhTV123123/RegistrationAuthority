package vn.ra.servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author THANH
 */
public class UploadFile extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UploadFile.class);
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
     * @throws org.apache.commons.fileupload.FileUploadException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, FileUploadException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession sessionsa = request.getSession(false);
            ConnectDatabase com = new ConnectDatabase();
            String strView = "";
            InputStream in = null;
            String fileUploaded = "";
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
                    //<editor-fold defaultstate="collapsed" desc="Content">
                    String contentType = request.getContentType();
                    if ((contentType != null) && (contentType.contains("multipart/form-data"))) {
                        String fileName;
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
                                    String root = getServletContext().getRealPath("/");
                                    File path = new File(root + "/uploads");
                                    if (!path.exists()) {
                                        boolean status = path.mkdirs();
                                    }
                                    File uploadedFile = new File(path + "/" + fileName);
                                    fileUploaded = path + "/" + fileName;
                                    item.write(uploadedFile);
                                }
                            }
                            in = new FileInputStream(fileUploaded);
                            strView = "0###" + new String(IOUtils.toByteArray(in));
                        }
                    }
                    //</editor-fold>
                } else if (sOutInner == 2) {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "###0";
                } else {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "###0";
                }
            } catch (IOException | NumberFormatException | SQLException e) {
                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###" + e.getMessage();
            } finally {
                if (in != null) {
                    in.close();
                }
                if ((new File(fileUploaded)).exists()) {
                    (new File(fileUploaded)).delete();
                }
            }
            out.println(strView);
        } finally {
            out.close();
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
        } catch (ServletException e) {
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } catch (IOException e) {
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } catch (SQLException e) {
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
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
        } catch (ServletException e) {
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } catch (IOException e) {
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } catch (SQLException e) {
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
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
