/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import vn.ra.utility.Definitions;

/**
 *
 * @author THANH
 */
public class DESEncryption {
    private static final String UNICODE_FORMAT = "UTF8";

    /**
     *
     */
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    private final KeySpec myKeySpec;
    private final SecretKeyFactory mySecretKeyFactory;
    private final Cipher cipher;
    byte[] keyAsBytes;
    private final String myEncryptionKey;
    private final String myEncryptionScheme;
    SecretKey key;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DESEncryption.class.getName());

    /**
     *
     * @throws Exception
     */
    public DESEncryption() throws Exception {
        myEncryptionKey = "111612102015";
        myEncryptionScheme = DES_ENCRYPTION_SCHEME;
        keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        myKeySpec = new DESKeySpec(keyAsBytes);
        mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = mySecretKeyFactory.generateSecret(myKeySpec);
    }

    /**
     * Method To Encrypt The String
     * @param unencryptedString
     * @return 
     */
    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            BASE64Encoder base64encoder = new BASE64Encoder();
            encryptedString = base64encoder.encode(encryptedText);
        } catch (InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            log.info(e.getMessage() + Definitions.CONFIG_LOG_WRITE_DOWNLINE, e);
        }
        return encryptedString;
    }

    /**
     * Method To Decrypt An Ecrypted String
     * @param encryptedString
     * @return 
     */
    public String decrypt(String encryptedString) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            BASE64Decoder base64decoder = new BASE64Decoder();
            byte[] encryptedText = base64decoder.decodeBuffer(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = bytes2String(plainText);
        } catch (InvalidKeyException | IOException | IllegalBlockSizeException | BadPaddingException e) {
            log.info(e.getMessage() + Definitions.CONFIG_LOG_WRITE_DOWNLINE, e);
        }
        return decryptedText;
    }

    /**
     * Returns String From An Array Of Bytes
     */
    private static String bytes2String(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }
}
