/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import java.io.ByteArrayInputStream;
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
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase.IOFileUploadException;
import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vn.mobileid.fms.client.JCRConfig;
import vn.mobileid.fms.client.JCRFile;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.FILE_PROFILE_DATA;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.ConnectFileToPartner;
import vn.ra.process.ConnectJackRabbitNew;
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
public class FileManageUploadEdit extends HttpServlet {
    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FileManageUploadEdit.class.getName());
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
            CommonFunction com = new CommonFunction();
            ConnectDatabase db = new ConnectDatabase();
            HttpSession sessionsa = request.getSession(false);
            String fileUploaded = "";
            String fileName;
            String fileMimeType;
            String sFILE_PROFILE = "";
            String sCERTIFICATION_ID = "";
            JSONArray listJson = new JSONArray();
//            boolean strAlert = false;
            try {
                int sOutInner;
                if (sessionsa != null) {
                    String strInnerUsername = sessionsa.getAttribute("sUserID").toString().trim();
                    String strInnerSessionKey = sessionsa.getAttribute("sesSessKey").toString().trim();
                    sOutInner = db.CheckIsLoginOnline(strInnerUsername, strInnerSessionKey);
                } else {
                    sOutInner = 2;
                }
                switch (sOutInner) {
                    case 1:
                        String sessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                        String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                        String sArrayFileExten = "";
                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                        if (sessGeneralPolicy[0].length > 0) {
                            for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_ALLOWED_FILE_EXTENSION_LIST)) {
                                    sArrayFileExten = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    break;
                                }
                            }
                        }   SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                        if (cartToken == null) {
                            cartToken = new SessionUploadFileCert();
                        }   Config conf = new Config();
                        String sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                        String sMax_SizeFile = conf.GetPropertybyCode(Definitions.CONFIG_JACK_RABBIT_MAX_LENGTH_FILE);
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
                                        fileName = item.getName().trim();
                                        if (CommonFunction.checkFileSpecial(fileName) == true) {
                                            if(CommonFunction.checkExtendAttachCertFile(fileName, sArrayFileExten) == true) {
                                                fileMimeType = getServletContext().getMimeType(FilenameUtils.getName(fileName));
                                                String sExtendFile = CommonFunction.getExtendFile(fileName);
                                                fileName = fileName.replace("." + sExtendFile, "") + "_" + CommonFunction.generateNumberDays() + "." + sExtendFile;
                                                String root = getServletContext().getRealPath("/");
                                                File path = new File(root + "/uploads");
                                                if (!path.exists()) {
                                                    boolean status = path.mkdirs();
                                                }
                                                File uploadedFile = new File(path + "/" + fileName);
                                                fileUploaded = path + "/" + fileName;
                                                item.write(uploadedFile);
                                                InputStream streamFile = new FileInputStream(fileUploaded);
                                                File file = new File(fileUploaded);
                                                double fileLength = 0;
                                                if(file.exists()) {
                                                    fileLength = file.length();
                                                }
                                                double defaultLength = Double.parseDouble(sMax_SizeFile);
                                                if(Double.compare(defaultLength * 1024, fileLength) > 0)
                                                {
                                                    FILE_PROFILE_DATA tempItem = new FILE_PROFILE_DATA();
                                                    tempItem.FILE_NAME = fileName;
                                                    tempItem.FILE_URL = CommonFunction.encodeFileToBase64Binary(new File(fileUploaded));
                                                    tempItem.FILE_MIMETYPE = fileMimeType;
                                                    tempItem.FILE_SIZE = fileLength;
                                                    tempItem.FILE_STREAM = IOUtils.toByteArray(streamFile);
                                                    tempList.add(tempItem);
                                                } else {
                                                    CommonFunction.LogErrorServlet(log, "Error Upload File Management: File size ("+ fileName +") is larger than specified");
                                                    sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_FILE_SIZE;
                                                    break;
                                                }
                                                streamFile.close();
                                            } else {
                                                sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_FILE_EXTENTION;
                                                break;
                                            }
                                        } else {
                                            sVALID_CODE = Definitions.CONFIG_EXCEPTION_STRING_ERROR_SPECIAL;
                                            break;
                                        }
                                    } else {
                                        if ("pFILE_PROFILE".equals(item.getFieldName())) {
                                            sFILE_PROFILE = item.getString();
                                        }
                                        if ("pCERTIFICATION_ID".equals(item.getFieldName())) {
                                            sCERTIFICATION_ID = item.getString();
                                        }
                                    }
                                }
                                if (sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                    if(tempList.size() > 0)
                                    {
                                        String sJRBConfig = "";
                                        if (sessGeneralPolicy[0].length > 0) {
                                            for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT))
                                                {
                                                    sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                    break;
                                                }
                                            }
                                        }
                                        //<editor-fold defaultstate="collapsed" desc="### JRB Process">
                                        String sOwnerID = "";
                                        CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(sCERTIFICATION_ID, sessLanguage, rsCert);
                                        if(rsCert[0].length > 0)
                                        {
                                            sOwnerID = String.valueOf(rsCert[0][0].CERTIFICATION_OWNER_ID);
                                        }
                                        String sJRB_Source =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
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
                                            String sFUNCTION_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_FUNCTION_UP);
                                            String idUUID_Temp = "4D4B4901-BF4C-4236-A9BA-AF2E0D4F5F33";
                                            for(int i = 0; i<tempList.size();i++)
                                            {
                                                String sFileData = EscapeUtils.CheckTextNull(tempList.get(i).FILE_URL);//CommonFunction.encodeFileToBase64Binary(fileUp);
                                                CloseableHttpResponse pHttpRes = ConnectFileToPartner.upFileParner(sIP_CONNECT, sHTTP_CONNECT,
                                                        sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                                                        sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, idUUID_Temp,
                                                        tempList.get(i).FILE_NAME, sFileData);
                                                InputStream isStr = pHttpRes.getEntity().getContent();
                                                String resultUUID = IOUtils.toString(isStr);
                                                CommonFunction.LogDebugString(log, "UUID", resultUUID);
                                                int[] pFILE_MANAGER_ID = new int[1];
                                                db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(sFILE_PROFILE, resultUUID, sJRBConfig,
                                                        EscapeUtils.CheckTextNull(tempList.get(i).FILE_MIMETYPE),
                                                        EscapeUtils.CheckTextNull(tempList.get(i).FILE_NAME), (int) tempList.get(i).FILE_SIZE,
                                                        sCERTIFICATION_ID, sOwnerID, loginUID, pFILE_MANAGER_ID);
                                                // insert temp file list
                                                FILE_PROFILE_DATA rsFILE_PROFILE = new FILE_PROFILE_DATA();
                                                rsFILE_PROFILE.FILE_MANAGER_ID = pFILE_MANAGER_ID[0];
                                                rsFILE_PROFILE.FILE_NAME = EscapeUtils.CheckTextNull(tempList.get(i).FILE_NAME);
                                                rsFILE_PROFILE.FILE_URL = EscapeUtils.CheckTextNull(tempList.get(i).FILE_URL);
                                                rsFILE_PROFILE.FILE_MIMETYPE = EscapeUtils.CheckTextNull(tempList.get(i).FILE_MIMETYPE);
                                                rsFILE_PROFILE.FILE_PROFILE = sFILE_PROFILE;
                                                rsFILE_PROFILE.FILE_SIZE = tempList.get(i).FILE_SIZE;
                                                rsFILE_PROFILE.FILE_STREAM = tempList.get(i).FILE_STREAM;
                                                cartToken.AddRoleFunctionsList(rsFILE_PROFILE);
                                            }
                                            request.getSession(false).setAttribute("sessUploadFileCert", cartToken);
                                        } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
                                            String sJRB_Host =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                            String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                            String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                            String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                            String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                            String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                            String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                            for(int i = 0; i<tempList.size();i++)
                                            {
                                                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                                                        Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                InputStream isFILE_STREAM = new ByteArrayInputStream(tempList.get(i).FILE_STREAM);
                                                JCRFile jrbFile = JackRabbitCommon.getInstance(jcrConfig).uploadFile(EscapeUtils.CheckTextNull(tempList.get(i).FILE_NAME),
                                                        EscapeUtils.CheckTextNull(tempList.get(i).FILE_MIMETYPE), isFILE_STREAM);
                                                if(jrbFile != null){
                                                    int[] pFILE_MANAGER_ID = new int[1];
                                                    db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(sFILE_PROFILE, jrbFile.getUuid(), sJRBConfig,
                                                            EscapeUtils.CheckTextNull(tempList.get(i).FILE_MIMETYPE),
                                                            jrbFile.getFileName(), (int) tempList.get(i).FILE_SIZE, sCERTIFICATION_ID, sOwnerID, loginUID, pFILE_MANAGER_ID);
                                                    FILE_PROFILE_DATA rsFILE_PROFILE = new FILE_PROFILE_DATA();
                                                    rsFILE_PROFILE.FILE_MANAGER_ID = pFILE_MANAGER_ID[0];
                                                    rsFILE_PROFILE.FILE_NAME = EscapeUtils.CheckTextNull(tempList.get(i).FILE_NAME);
                                                    rsFILE_PROFILE.FILE_URL = EscapeUtils.CheckTextNull(tempList.get(i).FILE_URL);
                                                    rsFILE_PROFILE.FILE_MIMETYPE = EscapeUtils.CheckTextNull(tempList.get(i).FILE_MIMETYPE);
                                                    rsFILE_PROFILE.FILE_PROFILE = sFILE_PROFILE;
                                                    rsFILE_PROFILE.FILE_SIZE = tempList.get(i).FILE_SIZE;
                                                    rsFILE_PROFILE.FILE_STREAM = tempList.get(i).FILE_STREAM;
                                                    cartToken.AddRoleFunctionsList(rsFILE_PROFILE);
                                                }
                                            }
                                            request.getSession(false).setAttribute("sessUploadFileCert", cartToken);
                                        } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_MID)) {
                                            String sJRB_Host =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                            String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                            String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                            String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                            String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                            String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                            String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                            for(int i = 0; i<tempList.size();i++)
                                            {
                                                InputStream isFILE_STREAM = new ByteArrayInputStream(tempList.get(i).FILE_STREAM);
                                                ConnectJackRabbitNew openJRB = new ConnectJackRabbitNew(sJRB_Host, sJRB_UserID, sJRB_UserPass,
                                                        Integer.parseInt(sJRB_MaxSession), Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                String[] sReturnJRB = new String[2];
                                                vn.mobileid.fms.client.JCRFile jrbFile = openJRB.uploadFile(EscapeUtils.CheckTextNull(tempList.get(i).FILE_NAME),
                                                        EscapeUtils.CheckTextNull(tempList.get(i).FILE_MIMETYPE), isFILE_STREAM, sReturnJRB);
                                                int[] pFILE_MANAGER_ID = new int[1];
                                                db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(sFILE_PROFILE, sReturnJRB[0].trim(), sJRBConfig,
                                                        EscapeUtils.CheckTextNull(tempList.get(i).FILE_MIMETYPE),
                                                        sReturnJRB[1].trim(), (int) tempList.get(i).FILE_SIZE, sCERTIFICATION_ID, sOwnerID, loginUID, pFILE_MANAGER_ID);
                                                FILE_PROFILE_DATA rsFILE_PROFILE = new FILE_PROFILE_DATA();
                                                rsFILE_PROFILE.FILE_MANAGER_ID = pFILE_MANAGER_ID[0];
                                                rsFILE_PROFILE.FILE_NAME = EscapeUtils.CheckTextNull(tempList.get(i).FILE_NAME);
                                                rsFILE_PROFILE.FILE_URL = EscapeUtils.CheckTextNull(tempList.get(i).FILE_URL);
                                                rsFILE_PROFILE.FILE_MIMETYPE = EscapeUtils.CheckTextNull(tempList.get(i).FILE_MIMETYPE);
                                                rsFILE_PROFILE.FILE_PROFILE = sFILE_PROFILE;
                                                rsFILE_PROFILE.FILE_SIZE = tempList.get(i).FILE_SIZE;
                                                rsFILE_PROFILE.FILE_STREAM = tempList.get(i).FILE_STREAM;
                                                cartToken.AddRoleFunctionsList(rsFILE_PROFILE);
                                            }
                                            request.getSession(false).setAttribute("sessUploadFileCert", cartToken);
                                        } else {
                                        }
                                        //</editor-fold>
                                        int j=1;
                                        ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                        for (FILE_PROFILE_DATA mhIP : ds) {
                                            if(mhIP.FILE_PROFILE.equals(sFILE_PROFILE))
                                            {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("Index", j++);
                                                json.put("FILE_MANAGER_ID", mhIP.FILE_MANAGER_ID);
                                                json.put("FILE_NAME", EscapeUtils.CheckTextNull(mhIP.FILE_NAME));
                                                json.put("FILE_PROFILE", EscapeUtils.CheckTextNull(mhIP.FILE_PROFILE));
                                                json.put("FILE_SIZE", com.convertMoneyFromDouble(mhIP.FILE_SIZE / 1024));
                                                listJson.add(json);
                                            }
                                        }   if (listJson.size() <= 0) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "1");
                                            listJson.add(json);
                                        }   break;
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", sVALID_CODE);
                                    listJson.add(json);
                                }
                            }
                        }
                    case 2:
                        {
                            JSONObject json = new JSONObject();
                            json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_LOGIN);
                            listJson.add(json);
                            break;
                        }
                    default:
                        {
                            JSONObject json = new JSONObject();
                            json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN);
                            listJson.add(json);
                            break;
                        }
                }
            } catch (MultipartStream.MalformedStreamException e) {
                log.error("MalformedStreamException: " + e.getMessage(), e);
                JSONObject json = new JSONObject();
                json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_ERROR);
                listJson.add(json);
            } catch (ServletException e) {
                log.error("ServletException: " + e.getMessage(), e);
                JSONObject json = new JSONObject();
                json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_ERROR);
                listJson.add(json);
            } catch (NumberFormatException e) {
                log.error("NumberFormatException: " + e.getMessage(), e);
                JSONObject json = new JSONObject();
                json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_ERROR);
                listJson.add(json);
            } catch (IOFileUploadException e) {
                log.error("IOFileUploadException: " + e.getMessage(), e);
                JSONObject json = new JSONObject();
                json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_ERROR);
                listJson.add(json);
            } catch (Exception e) {
                log.error("Exception: " + e.getMessage(), e);
                JSONObject json = new JSONObject();
                json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_ERROR);
                listJson.add(json);
            } finally {
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
