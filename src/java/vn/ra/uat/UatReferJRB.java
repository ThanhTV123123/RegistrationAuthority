/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import vn.mobileid.fms.client.JCRConfig;
import vn.mobileid.fms.client.JCRFile;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_AUTHORITY;
import vn.ra.object.FILE_MANAGER;
import vn.ra.object.FILE_PROFILE_DATA;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.JackRabbitCommon;
import vn.ra.process.SessionUploadFileCert;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.PropertiesContent;

/**
 *
 * @author THANH-PC
 */
public class UatReferJRB extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UatReferJRB.class);
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            ConnectDatabase db = new ConnectDatabase();
            String strView = "";
            FileOutputStream fileOuputStream = null;
            try {
                String idParam = EscapeUtils.CheckTextNull(request.getParameter("idParam"));
                if (null != idParam) {
                    switch (idParam) {
                        case "convertlicensetojrb": {
                            //<editor-fold defaultstate="collapsed" desc="convertlicensetojrb">
//                            Config conf = new Config();
                            String idUSR = EscapeUtils.CheckTextNull(request.getParameter("idUSR"));
                            String idPWD = EscapeUtils.CheckTextNull(request.getParameter("idPWD"));
                            String idCERT_ID = EscapeUtils.CheckTextNull(request.getParameter("idCERT_ID"));
                            if (!"".equals(idUSR) && !"".equals(idPWD)) {
                                if ("license".equals(idUSR) && "123abc!@#".equals(idPWD)) {
//                                    byte[] byteLicense = null;
//                                    int CREATE_BY = 0;
//                                    int CERTIFICATION_ID = 0;
//                                    String sCERT_SN = "";
//                                    String sFileName_Extend = "";
//                                    CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
//                                    db.S_BO_CERTIFICATION_GET_LICENSE_CONVERT(idCERT_ID, rsPgin);
//                                    if (rsPgin[0].length > 0) {
//                                        String sJRBConfig = "";
//                                        GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
//                                        db.S_BO_GENERAL_POLICY_LIST("1", rsPolicy);
//                                        if (rsPolicy[0].length > 0) {
//                                            for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
//                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT))
//                                                {
//                                                    sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                        // Insert File JackRabbit
//                                        String sJRB_Host =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
//                                        String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
//                                        String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
//                                        String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
//                                        String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
//                                        String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
//                                        String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
//                                        for(CERTIFICATION rsCert : rsPgin[0])
//                                        {
//                                            Blob blobLicense = rsCert.CERTIFICATION_LICENSE;
//                                            byteLicense = blobLicense.getBytes(1, (int) blobLicense.length());
//                                            sCERT_SN = EscapeUtils.CheckTextNull(rsCert.CERTIFICATION_SN);
//                                            CREATE_BY = rsCert.CREATED_BY_ID;
//                                            CERTIFICATION_ID = rsCert.ID;
//                                            sFileName_Extend = EscapeUtils.CheckTextNull(rsCert.TAX_CODE);
//                                            if ("".equals(sFileName_Extend)) {
//                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsCert.BUDGET_CODE);
//                                            }
//                                            if ("".equals(sFileName_Extend)) {
//                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsCert.P_ID);
//                                            }
//                                            if ("".equals(sFileName_Extend)) {
//                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsCert.PASSPORT);
//                                            }
//                                            if (byteLicense != null) {
//                                                String sNameFile = sCERT_SN + "_" + sFileName_Extend + Definitions.CONFIG_FILE_EXTENDTION_PDF;
//
//                                                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
//                                                    Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
//                                                InputStream isFILE_STREAM = new ByteArrayInputStream(byteLicense);
//                                                JCRFile jrbFile = JackRabbitCommon.uploadFile(jcrConfig, sNameFile, Definitions.CONFIG_MIMETYPE_PDF, isFILE_STREAM);
//                                                int[] pFILE_MANAGER_ID = new int[1];
////                                                db.S_BO_FILE_MANAGER_INSERT(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT, jrbFile.getUuid(),
////                                                    sJRBConfig, Definitions.CONFIG_MIMETYPE_PDF, jrbFile.getFileName(), (int) mhIP.FILE_SIZE, CERTIFICATION_ID, CREATE_BY, pFILE_MANAGER_ID);
//                                                //
//                                                strView = "0#0";
//                                            }
//                                        }
//                                    }
                                } else {
                                    strView = "1#Authentication Invalid";
                                }
                            } else {
                                strView = "1#Authentication Invalid";
                            }
                            //</editor-fold>
                            break;
                        }
                        case "createworkspace": {
                            //<editor-fold defaultstate="collapsed" desc="createworkspace">
//                            Config conf = new Config();
                            String idUSR = EscapeUtils.CheckTextNull(request.getParameter("idUSR"));
                            String idPWD = EscapeUtils.CheckTextNull(request.getParameter("idPWD"));
                            String idName = EscapeUtils.CheckTextNull(request.getParameter("idName"));
                            if (!"".equals(idUSR) && !"".equals(idPWD)) {
                                if ("workspace".equals(idUSR) && "123abc!@#".equals(idPWD)) {
                                    String sJRBConfig = "";
                                    GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
                                    db.S_BO_GENERAL_POLICY_LIST("1", rsPolicy);
                                    if (rsPolicy[0].length > 0)
                                    {
                                        for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
                                            if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT)) {
                                                sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                break;
                                            }
                                        }
                                    }
                                    log.info("createworkspace: " + sJRBConfig);
                                    // Insert File JackRabbit
                                    String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                    String sJRB_UserAdmin = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_ADMINID);
                                    String sJRB_PassAdmin = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_ADMIN_PASSWORD);
                                    String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                    String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                    String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                    String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                    String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                    String sJRB_WorkSpace = "";
                                    JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                                        Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                    log.info("createworkspace: " + idName + "; Admin: " + sJRB_UserAdmin + "; Pass: " + sJRB_PassAdmin);
                                    boolean boResult = JackRabbitCommon.getInstance(jcrConfig).createWorkSpace(sJRB_UserAdmin, sJRB_PassAdmin, idName);
                                    if(boResult == true) {
                                        strView = "0#Created Successfully";
                                    } else {
                                        strView = "1#Create has been Fail";
                                    }
                                } else {
                                    strView = "1#Authentication Invalid";
                                }
                            } else {
                                strView = "1#Authentication Invalid";
                            }
                            //</editor-fold>
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#Exception Error";
            } finally {
                if (fileOuputStream != null) {
                    fileOuputStream.close();
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(UatReferJRB.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (Exception ex) {
            Logger.getLogger(UatReferJRB.class.getName()).log(Level.SEVERE, null, ex);
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
