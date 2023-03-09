/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.HeadlessException;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.PropertyConfigurator;
import vn.mobileid.fms.client.JCRConfig;
import vn.mobileid.fms.client.JCRException;
import vn.mobileid.fms.client.JCRFile;
import vn.mobileid.fms.client.JCRManager;
import vn.mobileid.fms.client.loadbalancing.HealcheckConfig;
import vn.ra.utility.JackRabbitConfig;
import vn.ra.utility.SSLUtilities;

/**
 *
 * @author THANH-PC
 */
public class ConnectJackRabbitNew {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ConnectJackRabbitNew.class.getName());
//    static final String LOG_FILE = "bG9nNGoucm9vdExvZ2dlciA9IHRyYWNlLCBD"
//            + "T05TT0xFLCBpbmZvTG9nLCBlcnJvckxvZwpsb2c0ai5hcHBlbmRlci5pbmZvTG9nI"
//            + "D1vcmcuYXBhY2hlLmxvZzRqLkRhaWx5Um9sbGluZ0ZpbGVBcHBlbmRlcgpsb2c0ai"
//            + "5hcHBlbmRlci5pbmZvTG9nLkZpbGU9RDovL2xvZ3MvamNyX2luZm8ubG9nCmxvZzR"
//            + "qLmFwcGVuZGVyLmluZm9Mb2cudGhyZXNob2xkPWluZm8KbG9nNGouYXBwZW5kZXIu"
//            + "aW5mb0xvZy5sYXlvdXQ9b3JnLmFwYWNoZS5sb2c0ai5QYXR0ZXJuTGF5b3V0CmxvZ"
//            + "zRqLmFwcGVuZGVyLmluZm9Mb2cubGF5b3V0LmNvbnZlcnNpb25QYXR0ZXJuPUpDUi"
//            + "1UZXN0OiAlcCAlZHtkZE1NeXl5eSBISDptbTpzcy5TU1N9IFsldF0gKCVGOiVMKSB"
//            + "cdCVtJW4KbG9nNGouYXBwZW5kZXIuQ09OU09MRT1vcmcuYXBhY2hlLmxvZzRqLkNv"
//            + "bnNvbGVBcHBlbmRlcgpsb2c0ai5hcHBlbmRlci5DT05TT0xFLnRocmVzaG9sZD1pb"
//            + "mZvCmxvZzRqLmFwcGVuZGVyLkNPTlNPTEUubGF5b3V0PW9yZy5hcGFjaGUubG9nNG"
//            + "ouUGF0dGVybkxheW91dApsb2c0ai5hcHBlbmRlci5DT05TT0xFLmxheW91dC5Db25"
//            + "2ZXJzaW9uUGF0dGVybj1KQ1ItVGVzdDogJXAgJWR7ZGRNTXl5eXkgSEg6bW06c3Mu"
//            + "U1NTfSBbJXRdICAoJUY6JUwpIFx0JW0lbgpsb2c0ai5hcHBlbmRlci5lcnJvckxvZ"
//            + "yA9IG9yZy5hcGFjaGUubG9nNGouRGFpbHlSb2xsaW5nRmlsZUFwcGVuZGVyCmxvZz"
//            + "RqLmFwcGVuZGVyLmVycm9yTG9nLkZpbGUgPSBEOi8vbG9ncy9qY3JfZXJyb3IubG9"
//            + "nCmxvZzRqLmFwcGVuZGVyLmVycm9yTG9nLmxheW91dCA9IG9yZy5hcGFjaGUubG9n"
//            + "NGouUGF0dGVybkxheW91dApsb2c0ai5hcHBlbmRlci5lcnJvckxvZy5sYXlvdXQuQ"
//            + "29udmVyc2lvblBhdHRlcm49SkNSLVRlc3Q6ICVwICVke2RkTU15eXl5IEhIOm1tOn"
//            + "NzLlNTU30gWyV0XSAgKCVGOiVMKSBcdCVtJW4KbG9nNGouYXBwZW5kZXIuZXJyb3J"
//            + "Mb2cuVGhyZXNob2xkPUVSUk9S";
    
    JCRManager jcrManager;
     private JCRConfig getJCRConfig(String sHost, String sUser, String sPass, int intSession,
        int intMaxFile, String sWorkSpace, String sFolder) throws JCRException {
 
        JCRConfig jcrConfig = new JCRConfig();
        jcrConfig.setHost(sHost);
        jcrConfig.setUserID(sUser);
        jcrConfig.setPassword(sPass);
        jcrConfig.setMaxSession(intSession);
        jcrConfig.setMaxFileInFolder(intMaxFile);
        jcrConfig.setWorkSpaceName(sWorkSpace);
        jcrConfig.setFolderPrefix(sFolder);
//        jcrConfig.setHost("http://192.168.2.245:8080/FileManagerSystem/");
//        jcrConfig.setUserID("RWTRUONGNNT");
//        jcrConfig.setPassword("RWTRUONGNNT");
//        jcrConfig.setMaxSession(128);
//        jcrConfig.setMaxFileInFolder(10000);
//        jcrConfig.setWorkSpaceName("TRUONGNNT");
//        jcrConfig.setFolderPrefix("247_client_");
        return jcrConfig;
    }
    public ConnectJackRabbitNew(String sHost, String sUser, String sPass, int intSession,
        int intMaxFile, String sWorkSpace, String sFolder) throws FileNotFoundException {
        log.info("ConnectJackRabbitNew-Request: sHost" + sHost + "; sPass: " + sPass + "; intSession: " + intSession
            + "; intMaxFile: " + intMaxFile + "; sWorkSpace: " + sWorkSpace + "; sFolder: " + sFolder);
        System.out.println("Preparing to connect Servcer");
        addTrustedStore();
        try {
            JCRConfig jcrConfig = getJCRConfig(sHost, sUser, sPass, intSession,
                intMaxFile, sWorkSpace, sFolder);
            HealcheckConfig healcheckConfig = new HealcheckConfig();
            jcrManager = JCRManager.getInstance(jcrConfig, healcheckConfig);
//            PropertyConfigurator.configure(JackRabbitConfig.getResourceAsStream(LOG_FILE));
//            log.info("ConnectJackRabbitNew-Response: " + jcrManager.hello());
        } catch (JCRException ex) {
            log.error("ConnectJackRabbitNew: Can't connect to Server \n"+ex.getMessage(), ex);
        }
    }
    
//    public void downloadFile(String uuid, String savePath) {
//        long t0 = System.currentTimeMillis();
//        try {
//            downloadFile(jcrManager, uuid, savePath);
//        } catch (JCRException | IOException ex) {
//            System.out.println( ex.getMessage());
//        }
//        log.info("DownloadFile - Time for process: " + (System.currentTimeMillis() - t0));
//    }


//    public boolean uploadFile(String file, String fileName) {
//        long t0 = System.currentTimeMillis();
//        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(file))) {
//            uploadFile(jcrManager, fileName, inputStream);
//            log.info("UploadFile - Time for process: " + (System.currentTimeMillis() - t0));
//            return true;
//        }catch (IOException | JCRException ex) {
//            System.out.println( ex.getMessage());
//            return false;
//        }
//    } 
    
    public boolean createWorkspace(String host, String pswAdmin, String wsName){
        long t0 = System.currentTimeMillis();
        try {
            boolean check;
            check = JCRManager.createWorkspace(host, pswAdmin, wsName);
            log.info("CreateWorkspace - Time for process: " + (System.currentTimeMillis() - t0)); 
            return check;
        } catch (JCRException ex) {
            System.out.println( ex.getMsg());
            return false;
        }
    }
    
    public boolean changeAdminPass(String host, String pswAdmin, String newPsw){
        System.out.println("\n\nEnter admin-password: ");
        long t0 = System.currentTimeMillis();
        try {
            boolean check;
            check=JCRManager.changePasswordAdmin(host, pswAdmin, newPsw);
            log.info("ChangeAdminPass - Time for process: " + (System.currentTimeMillis() - t0));
            return check;
        } catch (JCRException ex) {
            System.out.println( ex.getMsg());
            return false;
        }   
    }
    
    public boolean changeWorkspacePass(String wsName, String loginUserId, String loginPsw,
        String changeUserId, String newPsw) {
        long t0 = System.currentTimeMillis();
        try {
            
            boolean check = jcrManager.changePasswordForWorkspace(wsName, loginUserId, loginPsw, changeUserId, newPsw);
            log.info("changeWorkspacePass - Time for process: " + (System.currentTimeMillis() - t0));
//            LOG.info("Time for process: " + (System.currentTimeMillis() - t0));
            return check;
        } catch (JCRException ex) {
            System.out.println( ex.getMsg());
            return false;
        }
    }
    
    public boolean addAcceptedIP(String wsName, String loginUserId, String loginPsw,
        String changeUserId, String[] acceptedIps)
    {
        try {
            boolean ret = jcrManager.addAcceptedIPForUser(wsName, loginUserId, loginPsw, changeUserId, acceptedIps);
            log.info("AddAcceptedIP - Add ip-address is " + ret);
            return ret;
        } catch (MalformedURLException | JCRException ex) {
            System.out.println( ex.getMessage()); 
            return false;
        }
    }
    
    public boolean repalceAcceptedIP(String wsName, String loginUserId,
        String loginPsw, String changeUserId, String[] acceptedIps)
    {
        try {
            boolean ret = jcrManager.replaceAcceptedIPForUser(wsName, loginUserId, loginPsw, changeUserId, acceptedIps);
            log.info("RepalceAcceptedIP -Replace ip-address is " + ret);
            return ret;
        } catch (HeadlessException | JCRException ex) {
            System.out.println( ex.getMessage());
            return false;
        }
    }
    
    public JCRFile uploadFile(String fileNameToSave, String sMimeType, InputStream stream, String[] sReturn)
        throws JCRException {
        if(!"".equals(fileNameToSave)) {
            fileNameToSave = CommonFunction.clearUnicodeFontString(fileNameToSave);
        }
        JCRFile uploadFile = new JCRFile();
        uploadFile.setFilePath(fileNameToSave);
        uploadFile.setMimeType(sMimeType);
        uploadFile.setStream(stream);
        JCRFile referFile = jcrManager.uploadJCR(uploadFile);
        if (referFile == null)
            return null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        log.info("UploadFile - jcrFile is: " + gson.toJson(referFile, JCRFile.class));
        sReturn[0] = referFile.getUuid();
        sReturn[1] = referFile.getFileName();
        return uploadFile;
    }
//    public JCRFile uploadFile(String fileNameToSave, String sMimeType, InputStream stream)
//        throws JCRException {
//        JCRFile uploadFile = new JCRFile();
//        uploadFile.setFilePath(fileNameToSave);
//        uploadFile.setMimeType(sMimeType);
//        uploadFile.setStream(stream);
//        JCRFile referFile = jcrManager.uploadJCR(uploadFile);
//        if (referFile == null)
//            return null;
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        
//        log.info("UploadFile - jcrFile is: " + gson.toJson(referFile, JCRFile.class));
//        return uploadFile;
//    }
    
    public JCRFile downloadFile(String uuid) throws JCRException, IOException
    {
        log.info("DownloadFile-Request: - UUID: " + uuid);
        JCRFile downloadFile = jcrManager.downloadJCR(uuid);
        log.info("DownloadFile-Response: - UUID: " + downloadFile.getUuid()+"; File Path: " + downloadFile.getFilePath()
            +"; Mime Type: " + downloadFile.getMimeType() +"; Last Modified: " + downloadFile.getLastModified()
            +"; Last Modified By: " + downloadFile.getLastModifiedBy() + "; File Name: " + downloadFile.getFileName());
//        try (OutputStream fOut = new FileOutputStream(savePath + "/" + downloadFile.getFileName())) {
//            IOUtils.write(IOUtils.toByteArray(downloadFile.getStream()), fOut);
//        }
        return downloadFile;
    }
    
    public void destroy(String[] args) {
        jcrManager.destroyAllJCR();
    }
    
    public static void addTrustedStore() {
        log.info("AddTrustedStore - Add all trust");
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
    }
    
    
}
