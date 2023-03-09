/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.utility.Config;
import vn.ra.utility.ConfigLog;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
public class DownFromSaveFile extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownFromSaveFile.class);
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
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        response.setContentType("text/html;charset=UTF-8");
        // (PrintWriter out = response.getWriter())
        try {
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
                String idParam = request.getParameter("idParam");
                if (null != idParam) {
                    switch (idParam) {
                        case "downfilesamplesim": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesamplesim">
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_SAMPLEFILE_TOKEN);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + sNameFile;//File.separator + request.getParameter("file");
                            CommonFunction.LogDebugString(log, "URL_FILE", absoluteDiskPath);
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "attachment;filename=" + conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_TOKEN));
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilesamplepush": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesamplepush">
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_SAMPLEFILE_PUSH);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + sNameFile;//File.separator + request.getParameter("file");
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "attachment;filename=" + conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_PUSH));
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilesamplecontrolprofile": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesamplecontrolprofile">
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_SAMPLECONTROL_PROFILE);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + sNameFile;//File.separator + request.getParameter("file");
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "attachment;filename=" + conf.GetPropertybyCode(Definitions.CONFIG_NAMECONTROL_PROFILE));
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilesamplecontactprofile": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesamplecontactprofile">
                            String sesSessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_SAMPLEFILE_CONTACT_PROFILE, sesSessLanguage);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + sNameFile;//File.separator + request.getParameter("file");
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "attachment;filename=" + conf.GetPrintPropertybyCode(Definitions.CONFIG_NAMEFILE_CONTACT_PROFILE, sesSessLanguage));
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilepdfcert": {
                            //<editor-fold defaultstate="collapsed" desc="downfilepdfcert">
                            String filename = EscapeUtils.CheckTextNull(request.getParameter("filename"));
                            filename = filename.replace("\\", "");
                            filename = filename.replace("/", "");
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + filename;
                            File f = new File(absoluteDiskPath);
                            if(f.exists()) {
                                try {
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    response.setContentType("application/octet-stream");
                                    response.setCharacterEncoding("utf-8");
                                    response.setHeader("Content-Disposition", "attachment;filename=" + filename);
                                    response.setContentLength((int) f.length());
                                    fileInputStream = new FileInputStream(f);
                                    byte[] buffer = new byte[1024];
                                    outs = response.getOutputStream();
                                    int i = 0;
                                    while ((i = fileInputStream.read(buffer)) != -1) {
                                        outs.write(buffer);
                                        outs.flush();
                                    }
                                } catch (Exception e) {
        //                            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                } finally {
                                    try {
                                        if (fileInputStream != null) {
                                            fileInputStream.close();
                                        }
                                        if (outs != null) {
                                            outs.close();
                                        }
                                    } catch (IOException e) {
                                        CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                    }
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilesamplelogo": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesamplelogo">
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_LOGO);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + "/Images/" + sNameFile;//File.separator + request.getParameter("file");
                            CommonFunction.LogDebugString(log, "URL_FILE", absoluteDiskPath);
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/octet-stream");
                                response.setHeader("Content-Disposition", "attachment;filename=" + sNameFile);
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfileexportquick_bk": {
                            //<editor-fold defaultstate="collapsed" desc="downfileexportquick_bk">
                            String absoluteDiskPath = request.getParameter("url");
                            absoluteDiskPath = absoluteDiskPath.replace("..\\", "");
                            absoluteDiskPath = absoluteDiskPath.replace("../", "");
                            String sNameFile = request.getParameter("name");
                            sNameFile = sNameFile.replace("\\", "");
                            sNameFile = sNameFile.replace("/", "");
                            FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/octet-stream");
                                response.setHeader("Content-Disposition", "attachment;filename=" + sNameFile);
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
    //                            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfileexportquick": {
                            //<editor-fold defaultstate="collapsed" desc="downfileexportquick">
                            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                            absoluteDiskPath = absoluteDiskPath.replace("..\\", "");
                            absoluteDiskPath = absoluteDiskPath.replace("../", "");
                            String sNameFile = EscapeUtils.CheckTextNull(request.getParameter("name"));
                            sNameFile = sNameFile.replace("\\", "");
                            sNameFile = sNameFile.replace("/", "");

                            File f = new File(absoluteDiskPath + sNameFile);
                            if(f.exists()) {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "inline; filename="+sNameFile);
                                response.setHeader("Pragma", "public");
                                response.setHeader("Cache-Control", "no-store");
                                response.addHeader("Cache-Control", "max-age=0");
                                FileInputStream fin = null;
                                try {
                                    fin = new FileInputStream(f);
                                } catch (FileNotFoundException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                                int size = 1024;
                                try {
                                    response.setContentLength(fin.available());
                                    byte[] buffer = new byte[size];
                                    ServletOutputStream os = null;
                                    os = response.getOutputStream();
                                    int length = 0;
                                     while ((length = fin.read(buffer)) != -1) {
                                        os.write(buffer, 0, length);
                                     }
                                    fin.close();
                                    os.flush();
                                    os.close();
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }

    //                        FileInputStream fileInputStream = null;
    //                        ServletOutputStream outs = null;
    //                        File f = new File(absoluteDiskPath);
    //                        try {
    ////                            response.setContentType("application/octet-stream");
    //                            response.setContentType("application/vnd.ms-excel");
    //                            response.setHeader("Content-Disposition", "attachment;filename=" + sNameFile);
    //                            response.setContentLength((int) f.length());
    //                            fileInputStream = new FileInputStream(f);
    //                            byte[] buffer = new byte[1024];
    //                            outs = response.getOutputStream();
    //                            int i = 0;
    //                            while ((i = fileInputStream.read(buffer)) != -1) {
    //                                outs.write(buffer);
    //                                outs.flush();
    //                            }
    //                        } catch (Exception e) {
    ////                            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
    //                        } finally {
    //                            try {
    //                                if (fileInputStream != null) {
    //                                    fileInputStream.close();
    //                                }
    //                                if (outs != null) {
    //                                    outs.close();
    //                                }
    //                            } catch (IOException e) {
    //                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
    //                            }
    //                        }
                            break;
                            //</editor-fold>
                        }
                        case "downfileword": {
                            //<editor-fold defaultstate="collapsed" desc="downfileword">
                            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);// request.getParameter("url");
                            absoluteDiskPath = absoluteDiskPath.replace("..\\", "");
                            absoluteDiskPath = absoluteDiskPath.replace("../", "");
                            String sNameFile = EscapeUtils.CheckTextNull(request.getParameter("name"));
                            sNameFile = sNameFile.replace("\\", "");
                            sNameFile = sNameFile.replace("/", "");
                            FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            File f = new File(absoluteDiskPath + sNameFile);
                            if(f.exists()) {
                                try {
                                    response.setContentType("application/msword");
                                    response.setHeader("Content-Disposition", "attachment;filename=" + sNameFile);
                                    response.setContentLength((int) f.length());
                                    fileInputStream = new FileInputStream(f);
                                    byte[] buffer = new byte[1024];
                                    outs = response.getOutputStream();
                                    int i = 0;
                                    while ((i = fileInputStream.read(buffer)) != -1) {
                                        outs.write(buffer);
                                        outs.flush();
                                    }
                                } catch (IOException e) {
        //                            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                } finally {
                                    try {
                                        if (fileInputStream != null) {
                                            fileInputStream.close();
                                        }
                                        if (outs != null) {
                                            outs.close();
                                        }
                                        if(new File(absoluteDiskPath).exists()) {
                                            new File(absoluteDiskPath).delete();
                                        }
                                    } catch (IOException e) {
                                        CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                    }
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfileimportpush": {
                            //<editor-fold defaultstate="collapsed" desc="downfileimportpush">
                            String sNameFile = EscapeUtils.CheckTextNull(request.getParameter("name"));
                            sNameFile = sNameFile.replace("\\", "");
                            sNameFile = sNameFile.replace("/", "");
                            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                            FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            File f = new File(absoluteDiskPath);
                            if(f.exists()) {
                                try {
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    response.setContentType("application/octet-stream");
                                    response.setHeader("Content-Disposition", "attachment;filename=" + sNameFile);
                                    response.setContentLength((int) f.length());
                                    fileInputStream = new FileInputStream(f);
                                    byte[] buffer = new byte[1024];
                                    outs = response.getOutputStream();
                                    int i = 0;
                                    while ((i = fileInputStream.read(buffer)) != -1) {
                                        outs.write(buffer);
                                        outs.flush();
                                    }
                                } catch (IOException e) {
                                } finally {
                                    try {
                                        if (fileInputStream != null) {
                                            fileInputStream.close();
                                        }
                                        if (outs != null) {
                                            outs.close();
                                        }
                                        if(new File(absoluteDiskPath).exists()) {
                                            new File(absoluteDiskPath).delete();
                                        }
                                    } catch (IOException e) {
                                        CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                    }
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfileimporttokenerror": {
                            //<editor-fold defaultstate="collapsed" desc="downfileimporttokenerror">
                            String sNameFile = EscapeUtils.CheckTextNull(request.getParameter("name"));
                            sNameFile = sNameFile.replace("\\", "");
                            sNameFile = sNameFile.replace("/", "");
                            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                            FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            File f = new File(absoluteDiskPath);
                            if(f.exists()) {
                                try {
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    response.setContentType("application/octet-stream");
                                    response.setCharacterEncoding("utf-8");
                                    response.setHeader("Content-Disposition", "attachment;filename=" + sNameFile);
                                    response.setContentLength((int) f.length());
                                    fileInputStream = new FileInputStream(f);
                                    byte[] buffer = new byte[1024];
                                    outs = response.getOutputStream();
                                    int i = 0;
                                    while ((i = fileInputStream.read(buffer)) != -1) {
                                        outs.write(buffer);
                                        outs.flush();
                                    }
                                } catch (IOException e) {
                                } finally {
                                    try {
                                        if (fileInputStream != null) {
                                            fileInputStream.close();
                                        }
                                        if (outs != null) {
                                            outs.close();
                                        }
                                        if(new File(absoluteDiskPath).exists()) {
                                            new File(absoluteDiskPath).delete();
                                        }
                                    } catch (IOException e) {
                                        CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                    }
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfileimportdisallowance": {
                            //<editor-fold defaultstate="collapsed" desc="downfileimportdisallowance">
                            String sNameFile = EscapeUtils.CheckTextNull(request.getParameter("name"));
                            sNameFile = sNameFile.replace("\\", "");
                            sNameFile = sNameFile.replace("/", "");
                            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                            FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            File f = new File(absoluteDiskPath);
                            if(f.exists()) {
                                try {
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    response.setContentType("application/octet-stream");
                                    response.setHeader("Content-Disposition", "attachment;filename=" + sNameFile);
                                    response.setContentLength((int) f.length());
                                    fileInputStream = new FileInputStream(f);
                                    byte[] buffer = new byte[1024];
                                    outs = response.getOutputStream();
                                    int i = 0;
                                    while ((i = fileInputStream.read(buffer)) != -1) {
                                        outs.write(buffer);
                                        outs.flush();
                                    }
                                } catch (IOException e) {
                                } finally {
                                    try {
                                        if (fileInputStream != null) {
                                            fileInputStream.close();
                                        }
                                        if (outs != null) {
                                            outs.close();
                                        }
                                        if(new File(absoluteDiskPath).exists()) {
                                            new File(absoluteDiskPath).delete();
                                        }
                                    } catch (IOException e) {
                                        CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                    }
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilesampledisallowance": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesampledisallowance">
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_SAMPLEFILE_DISALLOWANCE_LIST);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + sNameFile;//File.separator + request.getParameter("file");
                            CommonFunction.LogDebugString(log, "URL_FILE", absoluteDiskPath);
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "attachment;filename=" + conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_DISALLOWANCE_LIST));
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilesamplestaff": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesamplestaff">
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_SAMPLEFILE_REGISTERCERT_STAFF);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + sNameFile;//File.separator + request.getParameter("file");
    //                        CommonFunction.LogDebugString(log, "URL_FILE", absoluteDiskPath);
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "attachment;filename=" + conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_REGISTERCERT_STAFF));
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilesamplepersonal": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesamplepersonal">
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_SAMPLEFILE_REGISTERCERT_PERSONAL);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + sNameFile;//File.separator + request.getParameter("file");
    //                        CommonFunction.LogDebugString(log, "URL_FILE", absoluteDiskPath);
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "attachment;filename=" + conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_REGISTERCERT_PERSONAL));
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilesampleenterprise": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesampleenterprise">
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_SAMPLEFILE_REGISTERCERT_ENTERPRISE);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + sNameFile;//File.separator + request.getParameter("file");
                            CommonFunction.LogDebugString(log, "URL_FILE", absoluteDiskPath);
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "attachment;filename=" + conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_REGISTERCERT_ENTERPRISE));
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilesampleregistercert": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesampleregistercert">
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_SAMPLEFILE_REGISTERCERT_ALL);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + sNameFile;//File.separator + request.getParameter("file");
                            CommonFunction.LogDebugString(log, "URL_FILE", absoluteDiskPath);
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "attachment;filename=" + conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_REGISTERCERT_ALL));
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilesamplerenewcert": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesamplerenewcert">
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_SAMPLEFILE_RENEWCERT_ALL);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + sNameFile;//File.separator + request.getParameter("file");
                            CommonFunction.LogDebugString(log, "URL_FILE", absoluteDiskPath);
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "attachment;filename=" + conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_RENEWCERT_ALL));
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilesamplechangecert": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesamplechangecert">
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_SAMPLEFILE_CHANGECERT_ALL);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + sNameFile;//File.separator + request.getParameter("file");
                            CommonFunction.LogDebugString(log, "URL_FILE", absoluteDiskPath);
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "attachment;filename=" + conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_CHANGECERT_ALL));
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilesamplessuspend": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesamplessuspend">
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_SAMPLEFILE_SUSPENDCERT_ALL);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + sNameFile;//File.separator + request.getParameter("file");
                            CommonFunction.LogDebugString(log, "URL_FILE", absoluteDiskPath);
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "attachment;filename=" + conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_SUSPENDCERT_ALL));
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downfilesampletokenaction": {
                            //<editor-fold defaultstate="collapsed" desc="downfilesampletokenaction">
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_SAMPLEFILE_TOKENACTION_LIST);
                            String queryString = getServletContext().getRealPath("/");
                            String outputDirectory = queryString;
                            String absoluteDiskPath = outputDirectory + sNameFile;//File.separator + request.getParameter("file");
                            CommonFunction.LogDebugString(log, "URL_FILE", absoluteDiskPath);
                            File f = new File(absoluteDiskPath);
                            try {
                                response.setContentType("application/vnd.ms-excel");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Content-Disposition", "attachment;filename=" + conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_TOKENACTION_LIST));
                                response.setContentLength((int) f.length());
                                fileInputStream = new FileInputStream(f);
                                byte[] buffer = new byte[1024];
                                outs = response.getOutputStream();
                                int i = 0;
                                while ((i = fileInputStream.read(buffer)) != -1) {
                                    outs.write(buffer);
                                    outs.flush();
                                }
                            } catch (Exception e) {
                                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            } finally {
                                try {
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (outs != null) {
                                        outs.close();
                                    }
                                } catch (IOException e) {
                                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "downloadlog": {
                            //<editor-fold defaultstate="collapsed" desc="downloadlog">
                            String textDateLog = EscapeUtils.CheckTextNull(request.getParameter("textDateLog"));
                            String fileName = EscapeUtils.CheckTextNull(request.getParameter("textTypeLog"));
                            ConfigLog cnf = new ConfigLog();                            
                            String pathLogTemp = cnf.GetPropertybyCode("log");
                            pathLogTemp = pathLogTemp.replace("$", "");
                            pathLogTemp = pathLogTemp.replace("{", "");
                            pathLogTemp = pathLogTemp.replace("}", "");
                            pathLogTemp = pathLogTemp.replace("/", "");
                            String pathLog = System.getProperty(pathLogTemp).trim() + "/";
                            java.io.FileInputStream fileInputStream = null;
                            ServletOutputStream outs = null;
                            if (!"".equals(fileName)) {
                                String textDateAfter = CommonFunction.getDateDownServerLog(textDateLog);
                                if (!"".equals(textDateAfter)) {
                                    fileName = fileName + "." + textDateAfter;
                                }
                                pathLog = pathLog + fileName;
                                File f = new File(pathLog);
                                try {
                                    response.setContentType("text/plain");
                                    response.setCharacterEncoding("utf-8");
                                    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                                    response.setContentLength((int) f.length());
                                    fileInputStream = new FileInputStream(f);
                                    byte[] buffer = new byte[1024];
                                    outs = response.getOutputStream();
                                    int i = 0;
                                    while ((i = fileInputStream.read(buffer)) != -1) {
                                        outs.write(buffer);
                                        outs.flush();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        if (fileInputStream != null) {
                                            fileInputStream.close();
                                        }
                                        if (outs != null) {
                                            outs.close();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                    }
                }
            }
        } finally {
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
