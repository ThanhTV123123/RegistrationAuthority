/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vn.mobileid.fms.client.JCRConfig;
import vn.mobileid.fms.client.JCRFile;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.DNS_NAME_DATA;
import vn.ra.object.FILE_MANAGER;
import vn.ra.object.FILE_PROFILE_DATA;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.ConnectFileToPartner;
import vn.ra.process.ConnectJackRabbitNew;
import vn.ra.process.JackRabbitCommon;
import vn.ra.process.SessionDNSName;
import vn.ra.process.SessionUploadFileCert;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.PropertiesContent;

/**
 *
 * @author thanh
 */
public class FileManageCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FileManageCommon.class.getName());

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
            String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
            CommonFunction com = new CommonFunction();
            ConnectDatabase db = new ConnectDatabase();
            JSONArray listJson = new JSONArray();
            try {
                String idParam = request.getParameter("idParam");
                if (null != idParam) {
                    switch (idParam) {
                        case "deletetempfilecert": {
                            //<editor-fold defaultstate="collapsed" desc="deletetempfilecert">
                            String sFILE_PROFILE = request.getParameter("idType");
                            String sFILE_NAME = request.getParameter("vFILE_NAME");
                            SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                            if (cartToken != null) {
                                cartToken.DeleteFunctionList(sFILE_PROFILE, sFILE_NAME);
                                request.getSession(false).setAttribute("sessUploadFileCert", cartToken);
                                int j=1;
                                ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                for (FILE_PROFILE_DATA mhIP : ds) {
                                    if(mhIP.FILE_PROFILE.equals(sFILE_PROFILE))
                                    {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "0");
                                        json.put("Index", j++);
                                        json.put("FILE_NAME", mhIP.FILE_NAME);
                                        json.put("FILE_PROFILE", mhIP.FILE_PROFILE);
                                        json.put("FILE_SIZE", com.convertMoneyFromDouble(mhIP.FILE_SIZE / 1024));
                                        json.put("FILE_MANAGER_ID", String.valueOf(mhIP.FILE_MANAGER_ID));
                                        listJson.add(json);
                                    }
                                }
                            }
                            if (listJson.size() <= 0) {
                                JSONObject json = new JSONObject();
                                json.put("Code", "1");
                                listJson.add(json);
                            }
                            break;
                            //</editor-fold>
                        }
                        case "loadtempfilecert": {
                            //<editor-fold defaultstate="collapsed" desc="loadtempfilecert">
                            if(request.getSession(false).getAttribute("SessReRegisterCert") != null) {
                                String sesSessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                                String sSessReRegisterID = EscapeUtils.CheckTextNull(request.getSession(false).getAttribute("SessReRegisterCert").toString());
                                FILE_MANAGER[][] rsFileMana = null;
                                String sOWNER_ID = "";
                                CERTIFICATION[][] rs = new CERTIFICATION[1][];
                                db.S_BO_CERTIFICATION_DETAIL(sSessReRegisterID, sesSessLanguage, rs);
                                if (rs[0].length > 0) {
                                    sOWNER_ID = String.valueOf(rs[0][0].CERTIFICATION_OWNER_ID);
                                    rsFileMana = new FILE_MANAGER[1][];
                                    db.S_BO_FILE_MANAGER_GET_BY_CERTIFICATION_AND_OWNER(sSessReRegisterID, sOWNER_ID, sesSessLanguage, rsFileMana);
                                    SessionUploadFileCert cartToken = null;
                                    if(rsFileMana != null) {
                                        if(rsFileMana[0].length > 0) {
                                            cartToken = new SessionUploadFileCert();
                                            for(FILE_MANAGER rsFile : rsFileMana[0]) {
                                                InputStream inputStream = null;
                                                String sUUID = rsFile.UUID;
                                                String sJRBConfig = rsFile.DMS_PROPERTIES;
                                                String sJRB_Source =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
                                                //<editor-fold defaultstate="collapsed" desc="### JRB INPUT STREAM">
                                                if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_EFY))
                                                {
                                                    String sIP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_IP);
                                                    String sHTTP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PROTOCOL);
                                                    String sCONTEXT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_CONTEXT);
                                                    String sPORT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PORT);
                                                    String sDEFAULT_USER = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERNAME);
                                                    String sDEFAULT_PASS = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PASSWORD);
                                                    String sOWNERCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_OWNERCODE);
                                                    String sAPPCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_APPCODE);
                                                    String sFUNCTION_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_FUNCTION_DOWN);
                                                    CloseableHttpResponse pHttpRes = ConnectFileToPartner.loadFileParner(sIP_CONNECT, sHTTP_CONNECT,
                                                        sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                                                        sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, sUUID);
                                                    inputStream = pHttpRes.getEntity().getContent();
                                                } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
                                                    String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                    String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                    String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                    String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                    String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                    String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                    String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                    JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                                                        Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                    JCRFile jrbFile = JackRabbitCommon.getInstance(jcrConfig).downloadFile(sUUID);
                                                    if(jrbFile != null) {
                                                        if (jrbFile.getStream() != null) {
                                                            inputStream = jrbFile.getStream();
                                                        }
                                                    }
                                                } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_MID)) {
                                                    String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                    String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                    String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                    String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                    String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                    String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                    String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                    ConnectJackRabbitNew openJRB = new ConnectJackRabbitNew(sJRB_Host, sJRB_UserID, sJRB_UserPass,
                                                        Integer.parseInt(sJRB_MaxSession), Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                    vn.mobileid.fms.client.JCRFile jrbFile = openJRB.downloadFile(sUUID);
                                                    if (jrbFile.getStream() != null) {
                                                        inputStream = jrbFile.getStream();
                                                    }
                                                } else {
                                                }
                                                //</editor-fold>
                                            
                                                FILE_PROFILE_DATA item = new FILE_PROFILE_DATA();
                                                item.FILE_MANAGER_ID = rsFile.ID;
                                                item.FILE_NAME = rsFile.FILE_NAME;
                                                item.FILE_PROFILE = rsFile.FILE_PROFILE_NAME;
                                                item.FILE_SIZE = (double) rsFile.FILE_SIZE;
                                                item.FILE_MIMETYPE = rsFile.MIME_TYPE_NAME;
                                                if(inputStream!=null) {
                                                    item.FILE_STREAM = IOUtils.toByteArray(inputStream);
                                                } else {
                                                    item.FILE_STREAM = null;
                                                }
                                                cartToken.AddRoleFunctionsList(item);
                                            }
                                            request.getSession(false).setAttribute("sessUploadFileCert", cartToken);
                                        }
                                    }
                                    if (cartToken != null) {
                                        int j=1;
                                        ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                        for (FILE_PROFILE_DATA mhIP : ds) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("Index", j++);
                                            json.put("FILE_NAME", mhIP.FILE_NAME);
                                            json.put("FILE_PROFILE", mhIP.FILE_PROFILE);
                                            json.put("FILE_SIZE", com.convertMoneyFromDouble(mhIP.FILE_SIZE / 1024));
                                            json.put("FILE_MANAGER_ID", String.valueOf(mhIP.FILE_MANAGER_ID));
                                            listJson.add(json);
                                        }
                                        request.getSession(false).setAttribute("SessReRegisterCert",null);
                                    }
                                }
                            }
                            if (listJson.size() <= 0) {
                                JSONObject json = new JSONObject();
                                json.put("Code", "1");
                                listJson.add(json);
                            }
                            break;
                            //</editor-fold>
                        }
                        case "deletetempfilecertedit": {
                            //<editor-fold defaultstate="collapsed" desc="deletetempfilecertedit">
                            String sFILE_PROFILE = request.getParameter("idType");
                            String sFILE_NAME = request.getParameter("vFILE_NAME");
                            String pFILE_MANAGER_ID = request.getParameter("pFILE_MANAGER_ID");
                            SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                            if (cartToken != null) {
                                String sSessDelete = cartToken.DeleteFunctionList(sFILE_PROFILE, sFILE_NAME);
                                if("0".equals(sSessDelete)) {
                                    db.S_BO_FILE_MANAGER_DELETE(pFILE_MANAGER_ID, loginUID);
                                    request.getSession(false).setAttribute("sessUploadFileCert", cartToken);
                                    int j=1;
                                    ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                    for (FILE_PROFILE_DATA mhIP : ds) {
                                        if(mhIP.FILE_PROFILE.equals(sFILE_PROFILE))
                                        {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("Index", j++);
                                            json.put("FILE_MANAGER_ID", mhIP.FILE_MANAGER_ID);
                                            json.put("FILE_NAME", mhIP.FILE_NAME);
                                            json.put("FILE_PROFILE", mhIP.FILE_PROFILE);
                                            json.put("FILE_SIZE", com.convertMoneyFromDouble(mhIP.FILE_SIZE / 1024));
                                            listJson.add(json);
                                        }
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                            }
                            if (listJson.size() <= 0) {
                                JSONObject json = new JSONObject();
                                json.put("Code", "1");
                                listJson.add(json);
                            }
                            break;
                            //</editor-fold>
                        }
                        case "deletetempfilecertprofile": {
                            //<editor-fold defaultstate="collapsed" desc="deletetempfilecertprofile">
                            String sFILE_PROFILE = request.getParameter("idType");
                            String sFILE_NAME = request.getParameter("vFILE_NAME");
                            String pFILE_MANAGER_ID = request.getParameter("pFILE_MANAGER_ID");
                            SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCertProfile");
                            if (cartToken != null) {
                                String sSessDelete = cartToken.DeleteFunctionList(sFILE_PROFILE, sFILE_NAME);
                                if("0".equals(sSessDelete)) {
                                    db.S_BO_FILE_MANAGER_DELETE(pFILE_MANAGER_ID, loginUID);
                                    request.getSession(false).setAttribute("sessUploadFileCertProfile", cartToken);
                                    int j=1;
                                    ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                    for (FILE_PROFILE_DATA mhIP : ds) {
                                        if(mhIP.FILE_PROFILE.equals(sFILE_PROFILE))
                                        {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("Index", j++);
                                            json.put("FILE_MANAGER_ID", mhIP.FILE_MANAGER_ID);
                                            json.put("FILE_NAME", mhIP.FILE_NAME);
                                            json.put("FILE_PROFILE", mhIP.FILE_PROFILE);
                                            json.put("FILE_SIZE", com.convertMoneyFromDouble(mhIP.FILE_SIZE / 1024));
                                            json.put("SIGNED", mhIP.SIGNED ? "ON" : "OFF");
                                            String sModified = EscapeUtils.CheckTextNull(mhIP.MODIFIED_DT);
                                            json.put("MODIFIED_DT", "".equals(sModified) ? "..." : sModified);
                                            listJson.add(json);
                                        }
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                            }
                            if (listJson.size() <= 0) {
                                JSONObject json = new JSONObject();
                                json.put("Code", "1");
                                listJson.add(json);
                            }
                            break;
                            //</editor-fold>
                        }
                        // DNS NAME of SSL Cert
                        case "adddnsName": {
                            //<editor-fold defaultstate="collapsed" desc="adddnsName">
                            String idDNS = EscapeUtils.CheckTextNull(request.getParameter("idDNS"));
                            String idSessionDNS = EscapeUtils.CheckTextNull(request.getParameter("idSessionDNS"));
//                            SessionDNSName cartToken = (SessionDNSName) request.getSession(false).getAttribute("sessDNSNameForSSL");
                            SessionDNSName cartToken = (SessionDNSName) request.getSession(false).getAttribute(idSessionDNS);
                            if (cartToken == null) {
                                cartToken = new SessionDNSName();
                            }
                            ArrayList<DNS_NAME_DATA> tempList = new ArrayList<>();
                            DNS_NAME_DATA tempItem = new DNS_NAME_DATA();
                            tempItem.DNS_NAME = idDNS;
                            tempList.add(tempItem);
                            
                            if(tempList.size() > 0)
                            {
                                for(int i = 0; i<tempList.size();i++)
                                {
                                    DNS_NAME_DATA rsFILE_PROFILE = new DNS_NAME_DATA();
                                    rsFILE_PROFILE.DNS_NAME = EscapeUtils.CheckTextNull(tempList.get(i).DNS_NAME);
                                    cartToken.AddRoleFunctionsList(rsFILE_PROFILE);
                                }
                                request.getSession(false).setAttribute(idSessionDNS, cartToken);
                            }
                            int j=1;
                            ArrayList<DNS_NAME_DATA> ds = cartToken.getGH();
                            if(ds.size() > 0)
                            {
                                for (DNS_NAME_DATA mhIP : ds) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "0");
                                    json.put("Index", j++);
                                    json.put("DNS_NAME", EscapeUtils.CheckTextNull(mhIP.DNS_NAME));
                                    listJson.add(json);
                                }
                            } else {
                                CommonFunction.LogErrorServlet(log, "Error Upload File Management: empty file list");
                            }
                            if (listJson.size() <= 0) {
                                JSONObject json = new JSONObject();
                                json.put("Code", "1");
                                listJson.add(json);
                            }
                            break;
                            //</editor-fold>
                        }
                        case "deletednsName": {
                            //<editor-fold defaultstate="collapsed" desc="deletednsName">
                            String idDNS = EscapeUtils.CheckTextNull(request.getParameter("idDNS"));
                            String idSessionDNS = EscapeUtils.CheckTextNull(request.getParameter("idSessionDNS"));
                            SessionDNSName cartToken = (SessionDNSName) request.getSession(false).getAttribute(idSessionDNS);
                            if (cartToken != null) {
                                cartToken.DeleteFunctionList(idDNS);
                                request.getSession(false).setAttribute(idSessionDNS, cartToken);
                                int j=1;
                                ArrayList<DNS_NAME_DATA> ds = cartToken.getGH();
                                for (DNS_NAME_DATA mhIP : ds) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "0");
                                    json.put("Index", j++);
                                    json.put("DNS_NAME", mhIP.DNS_NAME);
                                    listJson.add(json);
                                }
                            }
                            if (listJson.size() <= 0) {
                                JSONObject json = new JSONObject();
                                json.put("Code", "1");
                                listJson.add(json);
                            }
                            break;
                            //</editor-fold>
                        }
                        case "signfilecertprofile": {
                            //<editor-fold defaultstate="collapsed" desc="signfilecertprofile">
                            String sFILE_PROFILE = EscapeUtils.CheckTextNull(request.getParameter("idType"));
                            String pFILE_MANAGER_ID = EscapeUtils.CheckTextNull(request.getParameter("pFILE_MANAGER_ID"));
                            String idCertID = request.getParameter("idCertID");
                            String idOwnerID = request.getParameter("idOwnerID");
                            SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCertProfile");
                            if (cartToken != null) {
                                String sessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                                FILE_MANAGER[][] rsFile;
                                rsFile = new FILE_MANAGER[1][];
                                db.S_BO_FILE_MANAGER_DETAIL(pFILE_MANAGER_ID, sessLanguage, rsFile);
                                if(rsFile[0].length > 0) {
                                    int[] intRes = new int[1];
                                    String[] sRes = new String[1];
                                    ConnectConnector.signFileProfile(pFILE_MANAGER_ID, intRes, sRes);
                                    if (intRes[0] == 0) {
                                        rsFile = new FILE_MANAGER[1][];
                                        db.S_BO_FILE_MANAGER_GET_BY_CERTIFICATION_AND_OWNER(idCertID, idOwnerID, sessLanguage, rsFile);
                                        if(rsFile[0].length > 0) {
                                            for(FILE_MANAGER item : rsFile[0]) {
                                                if(item.FILE_PROFILE_NAME.equals(sFILE_PROFILE)) {
                                                    cartToken.DeleteFunctionList(sFILE_PROFILE, item.FILE_NAME);
                                                    FILE_PROFILE_DATA itemNew = new FILE_PROFILE_DATA();
                                                    itemNew.FILE_MANAGER_ID = item.ID;
                                                    itemNew.FILE_NAME = item.FILE_NAME;
                                                    itemNew.FILE_PROFILE = item.FILE_PROFILE_NAME;
                                                    itemNew.FILE_SIZE = (double) item.FILE_SIZE;
                                                    itemNew.FILE_MIMETYPE = item.MIME_TYPE_NAME;
                                                    itemNew.FILE_STREAM = null;
                                                    itemNew.SIGNED = item.SIGNED;
                                                    itemNew.MODIFIED_DT = item.MODIFIED_DT;
                                                    cartToken.AddRoleFunctionsList(itemNew);
                                                }
                                            }
                                        }
                                        request.getSession(false).setAttribute("sessUploadFileCertProfile", cartToken);
                                        int j=1;
                                        ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                        for (FILE_PROFILE_DATA mhIP : ds) {
                                            if(mhIP.FILE_PROFILE.equals(sFILE_PROFILE))
                                            {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("Index", j++);
                                                json.put("FILE_MANAGER_ID", mhIP.FILE_MANAGER_ID);
                                                json.put("FILE_NAME", mhIP.FILE_NAME);
                                                json.put("FILE_PROFILE", mhIP.FILE_PROFILE);
                                                json.put("FILE_SIZE", com.convertMoneyFromDouble(mhIP.FILE_SIZE / 1024));
                                                json.put("SIGNED", mhIP.SIGNED ? "ON" : "OFF");
                                                String sModified = EscapeUtils.CheckTextNull(mhIP.MODIFIED_DT);
                                                json.put("MODIFIED_DT", "".equals(sModified) ? "..." : sModified);
                                                listJson.add(json);
                                            }
                                        }
                                    } else {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "2");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                            }
                            if (listJson.size() <= 0) {
                                JSONObject json = new JSONObject();
                                json.put("Code", "1");
                                listJson.add(json);
                            }
                            break;
                            //</editor-fold>
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.toString().trim(), e);
                JSONObject json = new JSONObject();
                json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_ERROR);
                listJson.add(json);
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
