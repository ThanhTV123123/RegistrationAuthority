/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.utility;

import vn.ra.process.CommonFunction;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;
import trustedhub.params.Cryptography;
import vn.ra.object.ProObj;

/**
 *
 * @author Tran
 */
public class PropertiesContent {

    /**
     *
     * @param propertiesContent
     * @return
     */
    public static ArrayList<ProObj> getPropertiesContent(String propertiesContent) {
        ArrayList<ProObj> result = new ArrayList<>();
        try {
            Properties p = new Properties();
            Reader reader = new InputStreamReader(new ByteArrayInputStream(propertiesContent.getBytes("UTF-8")), StandardCharsets.UTF_8);
            p.load(reader);
            
//            Properties p = new Properties();
//            p.load(new ByteArrayInputStream(propertiesContent.getBytes()));
            Enumeration em = p.keys();
            while (em.hasMoreElements()) {
                String key = (String) em.nextElement();
                String value = p.get(key).toString().trim();
//                CommonFunction.LogDebugString(null, "Value-01", value1);
//                String value = new String(((String) p.get(key)).getBytes(Definitions.CONFIG_UNICODE_UTF_8), StandardCharsets.UTF_8);
//                String value = new String(((String) p.get(key)).getBytes(Definitions.CONFIG_UNICODE_UTF_8));
//                String value = (String) p.get(key);
                ProObj ob = new ProObj(key, value);
                result.add(ob);
            }
        } catch (Exception ex) {
            CommonFunction.LogExceptionServlet(null, "PropertiesContent-getPropertiesContent: " + ex.getMessage(), ex);
        }
        return result;
    }

    /**
     *
     * @param propertiesContent
     * @param sKey
     * @return
     */
    public static String getPropertiesContentKey(String propertiesContent, String sKey) {
        String value = "";
        try {
            Properties p = new Properties();
            p.load(new ByteArrayInputStream(propertiesContent.getBytes()));
            Enumeration em = p.keys();
            while (em.hasMoreElements()) {
                String key = (String) em.nextElement();
                if (key.equals(sKey)) {
                    value = new String(((String) p.get(key)).getBytes("ISO-8859-1"), Definitions.CONFIG_UNICODE_UTF_8);
                }
            }
        } catch (Exception ex) {
            CommonFunction.LogExceptionServlet(null, "PropertiesContent-getPropertiesContentKey: " + ex.getMessage(), ex);
        }
        return value.trim();
    }
    
    public static String getPropertiesUtf8ContentKey(String propertiesContent, String sKey) {
//        Properties p = new Properties();
//        Reader reader = new InputStreamReader(new ByteArrayInputStream(emailTemplete.getBytes()), StandardCharsets.UTF_8);
//        p.load(reader);
//        String subject = p.getProperty(Constant.PATTERN_EMAIL_ISSUED_KEY_SUBJECT);
//        String content = p.getProperty(Constant.PATTERN_EMAIL_ISSUED_KEY_CONTENT);
                                                
        String value = "";
        try {
            Properties p = new Properties();
            Reader reader = new InputStreamReader(new ByteArrayInputStream(propertiesContent.getBytes()), StandardCharsets.UTF_8);
            p.load(reader);
            value = p.getProperty(sKey);
//            p.load(new ByteArrayInputStream(propertiesContent.getBytes()));
//            Enumeration em = p.keys();
//            while (em.hasMoreElements()) {
//                String key = (String) em.nextElement();
//                if (key.equals(sKey)) {
//                    value = new String(((String) p.get(key)).getBytes("ISO-8859-1"), Definitions.CONFIG_UNICODE_UTF_8);
//                }
//            }
        } catch (Exception ex) {
            CommonFunction.LogExceptionServlet(null, "PropertiesContent-getPropertiesContentKey: " + ex.getMessage(), ex);
        }
        return value.trim();
    }

    /**
     *
     * @param sValue
     * @param sSubject
     * @param sContent
     * @param sResult
     */
    public static void GetContentSendMail(String sValue, String sSubject, String sContent, String[] sResult) {
        try {
            sResult[0] = "0";
            int sSum = sValue.length();
            int sSumSub = sSubject.length();
            int sSumContent = sContent.length();
            int sSumCont = sValue.lastIndexOf(sContent);
            sResult[1] = sValue.substring(sSumSub, sSumCont);
            sResult[2] = sValue.substring(sSumCont + sSumContent, sSum);
        } catch (Exception ex) {
            sResult[0] = Definitions.CONFIG_EXCEPTION_STRING_ERROR;
            CommonFunction.LogExceptionServlet(null, "PropertiesContent-GetContentSendMail: " + ex.getMessage(), ex);
        }
    }

    /**
     *
     * @param sValue
     * @param sContent
     * @param sResult
     */
    public static void GetContentDisplayOneTAG(String sValue, String sContent, String[] sResult) {
        try {
            sResult[0] = "0";
            int sSum = sValue.length();
            int sSumContent = sContent.length();
            sResult[1] = sValue.substring(sSumContent, sSum);
        } catch (Exception ex) {
            sResult[0] = Definitions.CONFIG_EXCEPTION_STRING_ERROR;
            CommonFunction.LogExceptionServlet(null, "PropertiesContent-GetContentDisplayOneTAG: " + ex.getMessage(), ex);
        }
    }

    /**
     *
     * @param propertiesContent
     * @param key
     * @param value
     * @return
     */
    public static String insertPropertiesContent(String propertiesContent, String key, String value) {
        String result = "";
        try {
            CommonFunction.LogDebugString(null, "insertPropertiesContent", value);
            Properties p = new Properties();
            Reader reader = new InputStreamReader(new ByteArrayInputStream(propertiesContent.getBytes("UTF-8")), StandardCharsets.UTF_8);
            p.load(reader);

            p.setProperty(key, value);
            ByteArrayOutputStream bAos = new ByteArrayOutputStream();
            p.store(new OutputStreamWriter(bAos, "UTF-8"), "");
            result = new String(bAos.toByteArray(), "UTF-8");
//            Properties p = new Properties();
//            p.load(new ByteArrayInputStream(propertiesContent.getBytes()));
//            p.setProperty(key, new String(value.getBytes("ISO-8859-1"), "UTF-8"));
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            p.store(os, null);
//            result = new String(os.toByteArray());
        } catch (Exception ex) {
            CommonFunction.LogExceptionServlet(null, "PropertiesContent-InsertPropertiesContent: " + ex.getMessage(), ex);
        }
        return result;
    }

    /**
     *
     * @param propertiesContent
     * @param key
     * @param newValue
     * @return
     */
    public static String updatePropertiesContent(String propertiesContent, String key, String newValue) {
        String result = "";
        try {
            Properties p = new Properties();
            Reader reader = new InputStreamReader(new ByteArrayInputStream(propertiesContent.getBytes("UTF-8")), StandardCharsets.UTF_8);
            p.load(reader);

            p.setProperty(key, newValue);
            ByteArrayOutputStream bAos = new ByteArrayOutputStream();
            p.store(new OutputStreamWriter(bAos, "UTF-8"), "");
            result = new String(bAos.toByteArray(), "UTF-8");
            CommonFunction.LogDebugString(null, "updatePropertiesContent", newValue);
            /*
            Properties p = new Properties();
//            p.load(new ByteArrayInputStream(propertiesContent.getBytes("ISO-8859-1")));
            p.load(new ByteArrayInputStream(propertiesContent.getBytes()));
            p.setProperty(key,new String(newValue.getBytes("ISO-8859-1"), "UTF-8"));
//            p.store(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"), comment);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            p.store(os, null);
            result = new String(os.toByteArray());
             */

        } catch (Exception ex) {
            CommonFunction.LogExceptionServlet(null, "PropertiesContent-UpdatePropertiesContent: " + ex.getMessage(), ex);
        }
        return result;
    }

    /**
     *
     * @param propertiesContent
     * @param key
     * @return
     */
    public static String removePropertiesContent(String propertiesContent, String key) {
        String result = "";
        try {
            Properties p = new Properties();
            Reader reader = new InputStreamReader(new ByteArrayInputStream(propertiesContent.getBytes("UTF-8")), StandardCharsets.UTF_8);
            p.load(reader);
            p.remove(key);
//            Properties p = new Properties();
//            p.load(new ByteArrayInputStream(propertiesContent.getBytes()));
//            p.remove(key);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            p.store(os, null);
            result = new String(os.toByteArray());
        } catch (Exception ex) {
            CommonFunction.LogExceptionServlet(null, "PropertiesContent-RemovePropertiesContent: " + ex.getMessage(), ex);
        }
        return result;
    }

    /**
     *
     * @param res
     * @return
     */
    public static String getNewWorkerID(String res) {
        String id = "";
        try {
            String key = Definitions.CONFIG_WORKER_SUBSTRING_NEW_WOKERID;
            int index = res.indexOf(key);
            id = res.substring(index + key.length(), index + key.length() + 1);
            String nextId = res.substring(index + key.length() + 1, index + key.length() + 2);

            if (!nextId.equals(" ")) {
                int tmp = Integer.valueOf(id) * 10 + Integer.valueOf(nextId);
                id = String.valueOf(tmp);
            }
            String nextId2 = res.substring(index + key.length() + 2, index + key.length() + 3);
            if (!nextId2.equals(" ")) {
                int tmp = Integer.valueOf(id) * 10 + Integer.valueOf(nextId2);
                id = String.valueOf(tmp);
            }
        } catch (java.lang.NumberFormatException ex) {
            //CommonFunction.LogExceptionJava("PropertiesContent-getNewWorkerID: " + ex.getMessage(), ex);
        }
        return id.trim();
    }

    public static String encrypt(String plainText) {
        String result = null;
        try {
            result = DatatypeConverter.printBase64Binary(Cryptography.encryptTdes(plainText.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | NoSuchProviderException ex) {
            CommonFunction.LogExceptionServlet(null, "PropertiesContent-encrypt: " + ex.getMessage(), ex);
        }
        return result;
    }

    public static String decrypt(String encryptedText) {
        String result = null;
        try {
            result = new String(Cryptography.decryptTdes(DatatypeConverter.parseBase64Binary(encryptedText)));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | NoSuchProviderException ex) {
            CommonFunction.LogExceptionServlet(null, "PropertiesContent-decrypt: " + ex.getMessage(), ex);
        }
        return result;
    }
}
