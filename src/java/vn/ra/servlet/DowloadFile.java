package vn.ra.servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import vn.ra.process.CommonFunction;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
public class DowloadFile extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DowloadFile.class);
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
            throws ServletException, IOException, Exception {
//        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession sessionsa = request.getSession(false);
            ConnectDatabase com = new ConnectDatabase();
            int sOutInner;
            if (sessionsa != null) {
                String strInnerUsername = sessionsa.getAttribute("sUserID").toString().trim();
                String strInnerSessionKey = sessionsa.getAttribute("sesSessKey").toString().trim();
                sOutInner = com.CheckIsLoginOnline(strInnerUsername, strInnerSessionKey);
            } else {
                sOutInner = 2;
            }
            if (sOutInner == 1) {
                Config conf = new Config();
                java.io.FileInputStream fileInputStream = null;
                String filename = EscapeUtils.CheckTextNull(request.getParameter("filename"));
                filename = filename.replace("\\", "");
                filename = filename.replace("/", "");
    //            String queryString = getServletContext().getRealPath("/");
    //            String outputDirectory = queryString; outputDirectory
                String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + filename;
                try {
                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                    if (!directory.exists()){
                        directory.mkdir();
                    }
                    if(new File(absoluteDiskPath).exists()) {
        //                response.setContentType("text/html");
                        response.setContentType("APPLICATION/OCTET-STREAM");
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                        fileInputStream = new java.io.FileInputStream(absoluteDiskPath);
                        int i;
                        while ((i = fileInputStream.read()) != -1) {
                            out.write(i);
                        }
                    }
                } catch (Exception e) {
                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                } finally {
                    try {
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        if ((new File(absoluteDiskPath)).exists()) {
                            (new File(absoluteDiskPath)).delete();
                        }
                    } catch (IOException e) {
                        CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                    }
                }
            }
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
