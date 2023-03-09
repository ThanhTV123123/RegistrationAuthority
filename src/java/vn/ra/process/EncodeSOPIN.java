/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
//import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Thanh
 */
public class EncodeSOPIN {

    private static final Logger log = Logger.getLogger(EncodeSOPIN.class);

    /**
     *
     * @param encSOPIN
     * @return
     */
    public String decode(String encSOPIN) {
        String SO;
        try {
            if (encSOPIN.compareTo("") == 0) {
                log.error("Error: The encoded value doesn't exists.\n-----------------------------------");
                return null;
            }
            byte[] rawSO = DatatypeConverter.parseBase64Binary(encSOPIN);
            if ((rawSO.length % 8) != 0) {
                log.error("Error: The encoded value invalid .\n-----------------------------------");
                return null;
            }
            int length = rawSO.length;
            int numBlock = length / 8;
            byte[] block[] = new byte[numBlock][8];
            byte[] bt[] = new byte[numBlock][8];
            for (int i = 0; i < numBlock; i++) {
                System.arraycopy(rawSO, i * 8, block[i], 0, 8);
            }
            for (int i = 0; i < numBlock; i++) {
                bt[i] = decryptBlock(block[i]);
            }
            byte[] decRawSO = new byte[length];
            Arrays.fill(decRawSO, (byte) 0);
            for (int i = 0; i < numBlock; i++) {
                System.arraycopy(bt[i], 0, decRawSO, i * 8, 8);
            }
            int last_byte = decRawSO[length - 1];
            byte[] temp = new byte[length - last_byte];
            System.arraycopy(decRawSO, 0, temp, 0, length - last_byte);
            try {
                SO = new String(temp, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
                return null;
            }
        } catch (InvalidKeyException e) {
            log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
            return null;
        } catch (InvalidKeySpecException e) {
            log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
            return null;
        } catch (NoSuchAlgorithmException e) {
            log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
            return null;
        } catch (NoSuchPaddingException e) {
            log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
            return null;
        } catch (IllegalBlockSizeException e) {
            log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
            return null;
        } catch (BadPaddingException e) {
            log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
            return null;
        } catch (UnsupportedEncodingException e) {
            log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
            return null;
        }
        return SO;
    }

    /**
     *
     * @param SoPin
     * @return
     */
    public String encode(String SoPin) {
        if (SoPin.compareTo("") == 0) {
            log.error("Error: The Decoded value doesn't exists .\n-----------------------------------");
            return null;
        }
        byte[] rawSo;
        try {
            rawSo = SoPin.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
            return null;
        }
        int x, length, numBlock;
        String SOPIN;
        x = rawSo.length % 8;
        length = rawSo.length + 8 - x;
        numBlock = length / 8;
        byte[] dataSO = new byte[length];
        Arrays.fill(dataSO, (byte) (length - SoPin.length()));
        System.arraycopy(rawSo, 0, dataSO, 0, rawSo.length);
        byte[] block[] = new byte[numBlock][8];
        for (int i = 0; i < numBlock; i++) {
            System.arraycopy(dataSO, i * 8, block[i], 0, 8);
        }
        byte[] bt[] = new byte[numBlock][8];
        for (int i = 0; i < numBlock; i++) {
            try {
                bt[i] = encryptBlock(block[i]);
            } catch (InvalidKeyException e) {
                log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
                return null;
            } catch (InvalidKeySpecException e) {
                log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
                return null;
            } catch (NoSuchAlgorithmException e) {
                log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
                return null;
            } catch (NoSuchPaddingException e) {
                log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
                return null;
            } catch (IllegalBlockSizeException e) {
                log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
                return null;
            } catch (BadPaddingException e) {
                log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
                return null;
            } catch (UnsupportedEncodingException e) {
                log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
                return null;
            }
        }
        byte[] encSOPIN = new byte[length];
        for (int i = 0; i < numBlock; i++) {
            System.arraycopy(bt[i], 0, encSOPIN, i * 8, 8);
        }
        SOPIN = DatatypeConverter.printBase64Binary(encSOPIN);
        return SOPIN;
    }

    /**
     *
     * @param block
     * @return
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     */
    public byte[] encryptBlock(byte[] block) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] d1;
        if (block.length != 8) {
            d1 = null;
            return d1;
        }
        String key1 = "12345678";
        DESKeySpec KEY1 = new DESKeySpec(key1.getBytes("UTF-8"));
        SecretKeyFactory keyFactory1 = SecretKeyFactory.getInstance("DES");
        SecretKey myDesKey1 = keyFactory1.generateSecret(KEY1);
        String key2 = "90abcdef";
        DESKeySpec KEY2 = new DESKeySpec(key2.getBytes("UTF-8"));
        SecretKeyFactory keyFactory2 = SecretKeyFactory.getInstance("DES");
        SecretKey myDesKey2 = keyFactory2.generateSecret(KEY2);
        Cipher desCipher1;
        desCipher1 = Cipher.getInstance("DES/ECB/NoPadding");
        desCipher1.init(Cipher.ENCRYPT_MODE, myDesKey1);
        Cipher desCipher2;
        desCipher2 = Cipher.getInstance("DES/ECB/NoPadding");
        desCipher2.init(Cipher.DECRYPT_MODE, myDesKey2);
        byte[] d3 = desCipher1.doFinal(block);
        byte[] d2 = desCipher2.doFinal(d3);
        d1 = desCipher1.doFinal(d2);
        return d1;
    }

    /**
     *
     * @param block
     * @return
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     */
    public byte[] decryptBlock(byte[] block) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] d1;
        if (block.length != 8) {
            d1 = null;
            return d1;
        }
        String key1 = "12345678";
        DESKeySpec KEY1 = new DESKeySpec(key1.getBytes("UTF-8"));
        SecretKeyFactory keyFactory1 = SecretKeyFactory.getInstance("DES");
        SecretKey myDesKey1 = keyFactory1.generateSecret(KEY1);
        String key2 = "90abcdef";
        DESKeySpec KEY2 = new DESKeySpec(key2.getBytes("UTF-8"));
        SecretKeyFactory keyFactory2 = SecretKeyFactory.getInstance("DES");
        SecretKey myDesKey2 = keyFactory2.generateSecret(KEY2);
        Cipher desCipher1;
        desCipher1 = Cipher.getInstance("DES/ECB/NoPadding");
        desCipher1.init(Cipher.DECRYPT_MODE, myDesKey1);
        Cipher desCipher2;
        desCipher2 = Cipher.getInstance("DES/ECB/NoPadding");
        desCipher2.init(Cipher.ENCRYPT_MODE, myDesKey2);
        byte[] d3 = desCipher1.doFinal(block);
        byte[] d2 = desCipher2.doFinal(d3);
        d1 = desCipher1.doFinal(d2);
        return d1;
    }

    /**
     *
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     */
//    public String HassPass(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        String strResult;
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        md.update(password.getBytes("UTF-8"));
//        byte byteData[] = md.digest();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < byteData.length; i++) {
//            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
//        }
//        strResult = sb.toString();
//        return strResult;
//    }
    /**
     *
     * @param str
     * @return
     */
    public boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }

    /**
     *
     * @param str
     * @return
     */
    public boolean containsWhitespace(String str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param str
     * @return
     */
    public boolean lengthCharacter(String str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        return strLen >= 8;
    }

    /**
     *
     * @param str
     * @return
     */
    public boolean stringNmber(String str) { // true la qua
        if (!hasLength(str)) {
            return false;
        }
        Pattern p = Pattern.compile("([0-9])");
        Matcher m = p.matcher(str);
        boolean b = m.find();
        return b;
    }

    /**
     *
     * @param str
     * @return
     */
    public boolean stringCharater(String str) {
        if (!hasLength(str)) {
            return false;
        }
        Pattern p = Pattern.compile("([A-Za-z])");
        Matcher m = p.matcher(str);
        boolean b = m.find();
        return b;
    }

    /**
     *
     * @param str
     * @return
     */
    public boolean stringSpecial(String str) {
        if (!hasLength(str)) {
            return false;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(str);
        boolean b = m.find();
        return b;
    }

    /**
     *
     * @param strOld
     * @param strNew
     * @return
     */
    public boolean PassSamePass(String strOld, String strNew) {
        return !strNew.equals(strOld);
    }

    /**
     *
     * @param strPass
     * @param strUsername
     * @return
     */
    public boolean PassSameUsername(String strPass, String strUsername) {
        return !strPass.contains(strUsername);
    }

    /**
     *
     * @param strPass
     * @param strPhoneNumber
     * @return
     */
    public boolean PassSamePhoneNumber(String strPass, String strPhoneNumber) {
        return !strPass.contains(strPhoneNumber);
    }

    /**
     *
     * @param StrPass
     * @param StrFullname
     * @return
     */
    public boolean PassSameFullname(String StrPass, String StrFullname) {
        return !StrPass.contains(StrFullname);
    }

    /**
     *
     * @param strNum
     * @return
     */
    public boolean isNumeric(String strNum) {
        boolean isValid = false;
        String expression = "^[-+]?[0-9]*\\.?[0-9]+$";
        CharSequence inputStr = strNum;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     *
     * @param strNum
     * @return
     */
    public boolean isIndexOfFirst(String strNum) {
        boolean isValid = "0".equals(strNum.substring(0, 1));
        return isValid;
    }

    /**
     *
     * @param strNum
     * @param from
     * @param to
     * @return
     */
    public boolean isRangeNum(String strNum, int from, int to) {
        boolean isValid = strNum.length() >= from && strNum.length() <= to;
        return isValid;
    }

    /**
     *
     * @param strNum
     * @param num
     * @return
     */
    public boolean isRangeString(String strNum, int num) {
        boolean isValid = strNum.length() <= num;
        return isValid;
    }

    /**
     *
     * @param sValue
     * @param num
     * @param sText
     * @return
     */
    public String isNumericValid(String sValue, int num, String sText) {
        String sResult = "";
        if (sValue.length() > 0) {
            try {
                while (sValue.length() > 0) {
                    boolean sNum = isNumeric(sValue);
                    if (sNum == false) {
                        sResult = "1#" + sText + " only allow numeric value !";
                        break;
                    }
                    boolean sRange = isRangeString(sValue, num);
                    if (sRange == false) {
                        sResult = "1#Length of your " + sText + " maximum <= " + num + " character !";
                        break;
                    }
                    sResult = "0#0";
                    break;
                }
            } catch (Exception e) {
                log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
                sResult = "1#" + e.getMessage();
            }
        } else {
            sResult = "1#" + sText + " cann't be empty";
        }
        return sResult;
    }

    /**
     *
     * @param sMobile
     * @return
     */
    public String isMobileValid(String sMobile) {
        String sResult = "";
        if (sMobile.length() > 0) {
            try {
                while (sMobile.length() > 0) {
                    boolean sNum = isNumeric(sMobile);
                    if (sNum == false) {
                        sResult = "1#Phone Number only allow numeric value !";
                        break;
                    }
                    boolean sFirst = isIndexOfFirst(sMobile);
                    if (sFirst == false) {
                        sResult = "1#Phone Number should start with 0 !";
                        break;
                    }
                    boolean sRange = isRangeNum(sMobile, 8, 12);
                    if (sRange == false) {
                        sResult = "1#Phone Number ranging from 8 to 12 characters !";
                        break;
                    }
                    sResult = "0#0";
                    break;
                }
            } catch (Exception e) {
                log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
                sResult = "1#" + e.getMessage();
            }
        } else {
            sResult = "0#0";
        }
        return sResult;
    }

    /**
     *
     * @param email
     * @return
     */
    public boolean isEmailFormat(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     *
     * @param email
     * @param num
     * @return
     */
    public String isEmailValid(String email, int num) {
        String sResult = "";
        if (email.length() > 0) {
            try {
                while (email.length() > 0) {
                    boolean sFormat = isEmailFormat(email);
                    if (sFormat == false) {
                        sResult = "1#Email format is invalid !";
                        break;
                    }
                    boolean sRange = isRangeString(email, num);
                    if (sRange == false) {
                        sResult = "1#Length of your email maximum <= " + num + " character !";
                        break;
                    }
                    sResult = "0#0";
                    break;
                }
            } catch (Exception e) {
                log.error("Error: " + e.getMessage() + ".\n-----------------------------------", e);
                sResult = "1#" + e.getMessage();
            }
        } else {
            sResult = "1#Please Enter your Email !";
        }
        return sResult;
    }

    /**
     *
     * @param sSOPIN
     * @return
     */
    public String isSOPINValid(String sSOPIN) {
        String sResult;
        if (sSOPIN.length() > 0) {
            sResult = "0#0";
        } else {
            sResult = "1#SOPIN cann't be empty";
        }
        return sResult;
    }

    /**
     *
     * @param sValue
     * @param sKey
     * @return
     */
    public boolean CheckContainKey(String sValue, CharSequence sKey) {
        boolean sResult = sValue.contains(sKey);
        return sResult;
    }

    /**
     *
     * @param sValue
     * @return
     */
    public static String CheckTextNull(String sValue) {
        if (sValue == null) {
            sValue = "";
        } else {
            if ("NULL".equals(sValue.trim().toUpperCase())) {
                sValue = "";
            }
        }
        return sValue.trim();
    }

    /**
     *
     * @param sValue
     * @param intLength
     * @return
     */
    public static boolean CheckLengthImport(String sValue, int intLength) {
        boolean bResult = false;
        int intValue = sValue.trim().length();
        if (intValue > 0) {
            if (intValue <= intLength) {
                bResult = true;
            }
        }
        return bResult;
    }

    /**
     *
     * @param sValue
     * @return
     */
    public static String CheckReplaceImport(String sValue) {
        String sResult;
        sResult = sValue.replace("'", "").trim();
        return sResult;
    }

    /**
     *
     * @param strEnable
     * @param strContent
     * @return
     */
    public boolean CheckNotifiEmpty(String strEnable, String strContent) {
        boolean isValid = true;
        if ("1".equals(strEnable.trim())) {
            if ("".equals(strContent.trim())) {
                isValid = false;
            }
        }
        return isValid;
    }

//    public String HassPassV1(String password) throws NoSuchAlgorithmException {
//        String strResult;
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            md.update(password.getBytes());
//            byte byteData[] = md.digest();
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < byteData.length; i++) {
//                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
//            }
//            strResult = "0#" + sb.toString();
//        } catch (Exception e) {
//            strResult = "1#" + e.getMessage();
//        }
//        return strResult;
//    }
}
