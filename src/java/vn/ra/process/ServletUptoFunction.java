/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import vn.mobileid.fms.client.JCRConfig;
import vn.mobileid.fms.client.JCRFile;
import vn.ra.object.FILE_PROFILE_DATA;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.PropertiesContent;

/**
 *
 * @author USER because build failed of servlet class 21-04-2020
 */
public class ServletUptoFunction {

    public static void fileManagerInsert(String sJRBConfig, SessionUploadFileCert cartToken,
            String pCERTIFICATE_ID, String pCERTIFICATION_OWNER_ID, String loginUID,
            org.apache.log4j.Logger log, boolean isOldFileEnabled, boolean isFilterHandover, int[] intResult) throws IOException, SQLException, Exception {
        intResult[0] = 1;
        ConnectDatabase db = new ConnectDatabase();
        String sJRB_Source = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
        if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_EFY)) {
            if (cartToken != null) {
                String sIP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_IP);
                String sHTTP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PROTOCOL);
                String sCONTEXT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_CONTEXT);
                String sPORT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PORT);
                String sDEFAULT_USER = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERNAME);
                String sDEFAULT_PASS = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PASSWORD);
                String sOWNERCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_OWNERCODE);
                String sAPPCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_APPCODE);
                String sFUNCTION_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_FUNCTION_UP);
                String idUUID_Temp = Definitions.CONFIG_JACK_RABBIT_UUID_SAMPLE;
                ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                for (FILE_PROFILE_DATA mhIP : ds) {
                    boolean isCallEnabled = false;
                    if(isOldFileEnabled == true) {
                        isCallEnabled = true;
                    } else {
                        if(mhIP.FILE_MANAGER_ID == 0) {
                            isCallEnabled = true;
                        }
                    }
                    if(isCallEnabled == true) {
                        String sFileData = EscapeUtils.CheckTextNull(mhIP.FILE_URL);
                        CloseableHttpResponse pHttpRes = ConnectFileToPartner.upFileParner(sIP_CONNECT, sHTTP_CONNECT,
                                sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                                sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, idUUID_Temp,
                                mhIP.FILE_NAME, sFileData);
                        InputStream isStr = pHttpRes.getEntity().getContent();
                        String resultUUID = IOUtils.toString(isStr);
                        CommonFunction.LogDebugString(log, "UUID", resultUUID);
                        int[] pFILE_MANAGER_ID = new int[1];
                        db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, resultUUID, sJRBConfig,
                            EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE), mhIP.FILE_NAME,
                            (int) mhIP.FILE_SIZE, pCERTIFICATE_ID, pCERTIFICATION_OWNER_ID, loginUID, pFILE_MANAGER_ID);
                    }
                }
                intResult[0] = 0;
            }
        } else if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
            String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
            String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
            String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
            String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
            String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
            String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
            String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
            if (cartToken != null) {
                ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                for (FILE_PROFILE_DATA mhIP : ds) {
                    boolean isCallEnabled = false;
                    if(isOldFileEnabled == true) {
                        isCallEnabled = true;
                    } else {
                        if(mhIP.FILE_MANAGER_ID == 0) {
                            isCallEnabled = true;
                        }
                    }
                    if(isCallEnabled == true) {
                        if(isFilterHandover == true) {
                            if(mhIP.FILE_MANAGER_ID == 0) {
                                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                                    Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                InputStream isFILE_STREAM = new ByteArrayInputStream(mhIP.FILE_STREAM);
                                int[] pFILE_MANAGER_ID = new int[1];
                                JackRabbitCommon.getInstance(jcrConfig).uploadFileThread(mhIP.FILE_NAME, mhIP.FILE_MIMETYPE, isFILE_STREAM, mhIP, sJRBConfig,
                                    pCERTIFICATE_ID, pCERTIFICATION_OWNER_ID, loginUID, pFILE_MANAGER_ID);
                            } else {
                                if(mhIP.FILE_PROFILE.trim().equals(Definitions.CONFIG_FILE_PROFILE_PHOTO_ACTIVITY_DECLARATION)
                                    || mhIP.FILE_PROFILE.trim().equals(Definitions.CONFIG_FILE_PROFILE_PHOTO_ID_CARD)) {
                                    int[] pFILE_MANAGER_ID = new int[1];
                                    db.S_BO_FILE_MANAGER_INSERT_SHARE(mhIP.FILE_MANAGER_ID, Integer.parseInt(pCERTIFICATE_ID), loginUID, pFILE_MANAGER_ID);
                                }
                            }
                        } else {
                            JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                                    Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                            InputStream isFILE_STREAM = new ByteArrayInputStream(mhIP.FILE_STREAM);
                            int[] pFILE_MANAGER_ID = new int[1];
                            JackRabbitCommon.getInstance(jcrConfig).uploadFileThread(mhIP.FILE_NAME, mhIP.FILE_MIMETYPE, isFILE_STREAM, mhIP, sJRBConfig,
                                pCERTIFICATE_ID, pCERTIFICATION_OWNER_ID, loginUID, pFILE_MANAGER_ID);
                        }
                    }
                }
                intResult[0] = 0;
            }
        } else if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_MID)) {
            String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
            String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
            String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
            String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
            String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
            String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
            String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
            if (cartToken != null) {
                ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                for (FILE_PROFILE_DATA mhIP : ds) {
                    boolean isCallEnabled = false;
                    if(isOldFileEnabled == true) {
                        isCallEnabled = true;
                    } else {
                        if(mhIP.FILE_MANAGER_ID == 0) {
                            isCallEnabled = true;
                        }
                    }
                    if(isCallEnabled == true) {
                        ConnectJackRabbitNew openJRB = new ConnectJackRabbitNew(sJRB_Host, sJRB_UserID, sJRB_UserPass,
                                Integer.parseInt(sJRB_MaxSession), Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                        InputStream isFILE_STREAM = new ByteArrayInputStream(mhIP.FILE_STREAM);
                        String[] sReturnJRB = new String[2];
                        vn.mobileid.fms.client.JCRFile jrbFile = openJRB.uploadFile(EscapeUtils.CheckTextNull(mhIP.FILE_NAME),
                                EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE), isFILE_STREAM, sReturnJRB);
                        int[] pFILE_MANAGER_ID = new int[1];
                        db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, sReturnJRB[0].trim(), sJRBConfig,
                                EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE), sReturnJRB[1].trim(),
                                (int) mhIP.FILE_SIZE, pCERTIFICATE_ID, pCERTIFICATION_OWNER_ID, loginUID, pFILE_MANAGER_ID);
                    }
                }
                intResult[0] = 0;
            }
        } else {
        }
    }
}
