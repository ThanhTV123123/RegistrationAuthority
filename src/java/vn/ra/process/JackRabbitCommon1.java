///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package vn.ra.process;
//import java.io.FileOutputStream;
//import java.io.FilenameFilter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.sql.SQLException;
//
//import org.apache.commons.io.IOUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
//import java.io.InputStreamReader;
//import vn.mobileid.fms.client.JCRConfig;
//import vn.mobileid.fms.client.JCRException;
//import vn.mobileid.fms.client.JCRFile;
//import vn.mobileid.fms.client.JCRManager;
//import vn.mobileid.fms.client.loadbalancing.HealcheckConfig;
//
////import vn.mobileid.jackrabbit.function.JCRConfig;
////import vn.mobileid.jackrabbit.function.JCRException;
////import vn.mobileid.jackrabbit.function.JCRFile;
////import vn.mobileid.jackrabbit.function.JCRManager;
////import vn.mobileid.utils.Config;
///**
// *
// * @author thanh
// */
//public class JackRabbitCommon1 {
//    public static JCRConfig jcrConfig;
//    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JackRabbitCommon1.class.getName());
////    static final String CONFIG_FILE = "localconfig.properties";
//    public static boolean changePasswordWorkSpace(JCRConfig jcrConfig, String workspace, String loginId, String loginPsw, String userId, String newPsw) throws JCRException {
//        HealcheckConfig healcheckConfig = new HealcheckConfig();
//        JCRManager jcrManager = JCRManager.getInstance(jcrConfig, healcheckConfig);
//        return jcrManager.changePasswordForWorkspace(workspace, loginId, loginPsw, userId, newPsw);
//    }
//
//    public static boolean changePasswordAdmin(JCRConfig jcrConfig, String oldPsw, String newPsw) throws JCRException {
//        HealcheckConfig healcheckConfig = new HealcheckConfig();
//        JCRManager jcrManager = JCRManager.getInstance(jcrConfig, healcheckConfig);
//        return jcrManager.changePasswordAdmin(oldPsw, newPsw);
//    }
//
//    public static boolean createWorkSpace(JCRConfig jcrConfig, String userAdmin, String pswAdmin, String wsName) throws JCRException {
//        HealcheckConfig healcheckConfig = new HealcheckConfig();
//        JCRManager jcrManager = JCRManager.getInstance(jcrConfig, healcheckConfig);
//        return jcrManager.createWorkspace(userAdmin, pswAdmin, wsName);
////        return jcrManager.createWorkspace("admin", pswAdmin, wsName);
//    }
//
//    public static JCRFile uploadFile(JCRConfig jcrConfigInner, String fileNameToSave,
//        String sMimeType, InputStream stream) {
//        JCRFile referFile = null;
//        JCRManager jcrManager = null;
//        HealcheckConfig healcheckConfig = new HealcheckConfig();
//        try {
//            jcrManager = JCRManager.getInstance(jcrConfig, healcheckConfig);
//            if(!"".equals(fileNameToSave)) {
//                fileNameToSave = CommonFunction.clearUnicodeFontString(fileNameToSave);
//            }
//            CommonFunction.LogDebugString(log, "uploadFile-JRB", jcrConfig.getHost() + "; fileNameToSave: " + fileNameToSave);
//            JCRFile uploadFile = new JCRFile();
//            uploadFile.setFilePath(fileNameToSave); // + ".pdf"
//            uploadFile.setMimeType(sMimeType); //"application/pdf"
//            uploadFile.setStream(stream);
//            referFile = jcrManager.uploadJCR(uploadFile);
//    //        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//    //        log.info("jcrFile is: " + gson.toJson(referFile, JCRFile.class));
//        } catch(Exception ex) {
//            CommonFunction.LogExceptionServlet(log, "FileName: " + fileNameToSave + "; " + ex.getMessage(), ex);
//            if(jcrManager != null){
//                jcrManager.destroy();
//                JCRManager.destroyAllJCR();
//                CommonFunction.LogDebugString(log, "uploadFile-JRB", "Destroy");
//            }
//            if(jcrConfig != null) {
//                jcrConfig = null;
//                CommonFunction.LogDebugString(log, "uploadFile-JRB-jcrConfig", "null");
//            }
//        }
////        finally {
////            if(jcrConfig != null) {
////                jcrConfig = null;
////                CommonFunction.LogDebugString(log, "uploadFile-JRB-jcrConfig", "null");
////            }
////        }
//        return referFile;
//    }
//
//    public static JCRFile downloadFile(JCRConfig jcrConfigInner, String uuid) {
//        JCRFile downloadFile = null;
//        JCRManager manager = null;
//        try {
//            HealcheckConfig healcheckConfig = new HealcheckConfig();
//            CommonFunction.LogDebugString(log, "downloadFile-JRB", jcrConfig.getHost() + "; uuid: " + uuid);
//            manager = JCRManager.getInstance(jcrConfig, healcheckConfig);
//    //        LOG.info("Get JCRManager instance successful...");
//            downloadFile = manager.downloadJCR(uuid);
//    //        LOG.info("UUID : " + downloadFile.getUuid());
//    //        LOG.info("File Path : " + downloadFile.getFilePath());
//    //        LOG.info("Mime Type : " + downloadFile.getMimeType());
//    //        LOG.info("Last Modified : " + downloadFile.getLastModified());
//    //        LOG.info("Last Modified By : " + downloadFile.getLastModifiedBy());
//    //        LOG.info("File Name : " + downloadFile.getFileName());
//
//    //        OutputStream fOut = new FileOutputStream("E:/Project/TMS CA/TMS EFY/Demo JackRabbit/" + downloadFile.getFileName());
//    //        IOUtils.write(IOUtils.toByteArray(downloadFile.getStream()), fOut);
//    //        fOut.close();
//        } catch(Exception ex) {
//            CommonFunction.LogExceptionServlet(log, "UUID: " + uuid + "; " + ex.getMessage(), ex);
//            if(manager != null){
//                manager.destroy();
//                JCRManager.destroyAllJCR();
//                CommonFunction.LogDebugString(log, "downloadFile-JRB", "Destroy");
//            }
//            if(jcrConfig != null){
//                jcrConfig = null;
//                CommonFunction.LogDebugString(log, "downloadFile-JRB-jcrConfig", "null");
//            }
//            //JCRManager.destroyAllJCR();
//        }
////        finally {
////            if(jcrConfig != null) {
////                jcrConfig = null;
////                CommonFunction.LogDebugString(log, "downloadFile-JRB-jcrConfig", "null");
////            }
////        }
//        return downloadFile;
//    }
//
////    static final String JCR_HOST = "jcr.config.host";
////    static final String JCR_USER_ID = "jcr.config.userid";
////    static final String JCR_PWD = "jcr.config.password";
////    static final String JCR_MAX_SESSION = "jcr.config.maxSession";
////    static final String JCR_MAX_FILE_IN_FOLDER = "jcr.config.maxFileInFolder";
////    static final String JCR_MAX_PREFIX_FOLDER = "jcr.config.prefixFolder";
////    static final String JCR_WORKSPACE = "jcr.config.workspace";
//
//    public static JCRConfig getJCRConfig(String sJCR_HOST, String sJCR_USER_ID, String sJCR_PWD,
//            int sJCR_MAX_SESSION, int sJCR_MAX_FILE_IN_FOLDER, String sJCR_WORKSPACE,
//            String sJCR_MAX_PREFIX_FOLDER) throws SQLException, JCRException {
//        CommonFunction.LogDebugString(log, "getJCRConfig-Request", "sJCR_HOST: " + sJCR_HOST + "; sJCR_USER_ID: " + sJCR_USER_ID
//            + "; sJCR_PWD: " + sJCR_PWD + "; sJCR_MAX_SESSION: " + sJCR_MAX_SESSION + "; sJCR_MAX_FILE_IN_FOLDER: " + sJCR_MAX_FILE_IN_FOLDER
//            + "; sJCR_WORKSPACE: " + sJCR_WORKSPACE + "; sJCR_MAX_PREFIX_FOLDER: " + sJCR_MAX_PREFIX_FOLDER);
//        if(jcrConfig == null)
//        {
//            CommonFunction.LogDebugString(log, "Session-JRB: ", "NULL");
//            jcrConfig = new JCRConfig();
//            if(sJCR_MAX_SESSION == 0) {
//                sJCR_MAX_SESSION = 50;
//            }
//            if(sJCR_MAX_FILE_IN_FOLDER == 0) {
//                sJCR_MAX_FILE_IN_FOLDER = 10000;
//            }
//            if("".equals(sJCR_WORKSPACE)) {
//                sJCR_WORKSPACE = "default";
//            }
//            if("".equals(sJCR_MAX_PREFIX_FOLDER)) {
//                sJCR_MAX_PREFIX_FOLDER = "247_client_";
//            }
//            jcrConfig.setHost(sJCR_HOST);
//            jcrConfig.setUserID(sJCR_USER_ID);
//            jcrConfig.setPassword(sJCR_PWD);
//            jcrConfig.setMaxSession(sJCR_MAX_SESSION);
//            jcrConfig.setMaxFileInFolder(sJCR_MAX_FILE_IN_FOLDER);
//            jcrConfig.setWorkSpaceName(sJCR_WORKSPACE);
//            jcrConfig.setFolderPrefix(sJCR_MAX_PREFIX_FOLDER);
//        }
//    //        jcrConfig.setHost(cfg.getProperty(JCR_HOST));
//    //        jcrConfig.setUserID(cfg.getProperty(JCR_USER_ID));
//    //        jcrConfig.setPassword(cfg.getProperty(JCR_PWD));
//    //        jcrConfig.setMaxSession(cfg.getIntProperty(JCR_MAX_SESSION, 50));
//    //        jcrConfig.setMaxFileInFolder(cfg.getIntProperty(JCR_MAX_FILE_IN_FOLDER, 10000));
//    //        jcrConfig.setWorkSpaceName(cfg.getStringProperty(JCR_WORKSPACE, "default"));
//
//    //        String prefix = Config.getInstance(CONFIG_FILE).getStringProperty(JCR_MAX_PREFIX_FOLDER, "247_client_");
//    //        jcrConfig.setFolderPrefix(sJCR_MAX_PREFIX_FOLDER);
//
//        return jcrConfig;
//    }
//}
