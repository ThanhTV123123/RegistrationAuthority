/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import vn.ra.object.ATTRIBUTE_VALUES;
import vn.ra.object.CERTIFICATE_ATTRIBUTES;
import vn.ra.object.CERTIFICATION_COMMENT;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.CERTIFICATION_PROPERTIES_JSON;
import vn.ra.object.ROLE_DATA;
import vn.ra.object.ROLE_ATTRIBUTES;
import vn.ra.object.SOAPSecureProperties;
import vn.ra.process.CommonFunction;
import vn.ra.process.DESEncryption;
import vn.ra.process.EncodeSOPIN;
import vn.ra.process.GenFeatureCertificate;
import vn.ra.process.GetFeatureCertificate2;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.PropertiesContent;
import vn.ra.ws.RAServiceResp;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_TYPE_COMPONENT;
import vn.ra.object.CertificateInfo;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.ProfileContactInfoJson;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.ConnectDbPhaseTwo;
import vn.ra.utility.Config;
import vn.ra.utility.LoadParamSystem;

/**
 *
 * @author THANH-PC
 */
public class Test {

    public static String convertDateTimeToString(String strDate, String strParternOne, String strParternTwo) {
        try {
            Date date1 = new SimpleDateFormat(strParternOne, Locale.getDefault()).parse(strDate);
            DateFormat dfDate = new SimpleDateFormat(strParternTwo);
            return dfDate.format(date1);
        } catch (ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    public static String convertDateTimeToDate(Date strDate, String strParternOne, String strParternTwo) {
        DateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dfDate.format(strDate);
    }

    public static void main(String[] args) throws Exception {
        String strDate = "2022-10-26T10:46:22.14+00:00";
        String strPartern1 = "yyyy-MM-dd'T'HH:mm:ss";
        String strPartern2 = "dd/MM/yyyy HH:mm:ss";
        String sResult = convertDateTimeToString(strDate, strPartern1, strPartern2);
        System.out.println("KQ: " + sResult);
        
    //DESEncryption crypt = new DESEncryption();
    //String sLog

    //ConnectDatabase db = new ConnectDatabase();

    /*String strDate = "12-12-2022";
        CommonFunction coms = new CommonFunction();
        System.out.println(coms.formatDateTimeToDateTime1(strDate));*/
//        int[] pRESPONSE_CODE = new int[1];
//        CertificateInfo[][] rsReq = new CertificateInfo[1][];
//        db.S_BO_API_CERTIFICATION_GET_INFO("", "", "", "", "", 11850, "", "",
//                1, pRESPONSE_CODE, rsReq, "", "", "", "");
//        if (rsReq[0].length > 0) {
//            System.out.println(rsReq[0][0].certificateSN);
//            System.out.println(rsReq[0][0].requestStateCode);
//            System.out.println(rsReq[0][0].requestTypeCode);
//            System.out.println(rsReq[0][0].certificateStateId);
//            System.out.println(rsReq[0][0].formFactorCode);
//        }
    /*String sCertProfileProperties = "{\"attributes\": [{\"require\": true, \"attributeType\": \"UID_LIST/COMPANY\", \"attributes\": [{\"name\": \"0.9.2342.19200300.100.1.1\", \"remarkEn\": \"Unique Identifier (Tax code)\", \"remark\": \"Mã số thuế\", \"prefix\": \"MST:\", \"require\": true, \"attributeType\": \"TEXT_FIELD\"}, {\"name\": \"0.9.2342.19200300.100.1.1\", \"remarkEn\": \"Unique Identifier (Budget ID)\", \"remark\": \"Mã ngân sách\", \"prefix\": \"MNS:\", \"require\": true, \"attributeType\": \"TEXT_FIELD\"}, {\"name\": \"0.9.2342.19200300.100.1.1\", \"remarkEn\": \"Decision\", \"remark\": \"Quyết định\", \"prefix\": \"QĐ:\", \"require\": true, \"attributeType\": \"TEXT_FIELD\"},{\"name\":\"0.9.2342.19200300.100.1.1\",\"remarkEn\":\"Social Insurance\",\"remark\":\"Bảo hiểm xã hội\",\"prefix\":\"BHXH:\",\"require\":true,\"attributeType\":\"TEXT_FIELD\"},{\"name\":\"0.9.2342.19200300.100.1.1\",\"remarkEn\":\"Unit Code\",\"remark\":\"Mã đơn vị\",\"prefix\":\"MDV:\",\"require\":true,\"attributeType\":\"TEXT_FIELD\"}]}, {\"require\": true, \"attributeType\": \"UID_LIST/PERSONAL\", \"attributes\": [{\"name\": \"0.9.2342.19200300.100.1.1\", \"remarkEn\": \"Unique Identifier (Personal ID)\", \"remark\": \"Chứng minh nhân dân\", \"prefix\": \"CMND:\", \"require\": true, \"attributeType\": \"TEXT_FIELD\"}, {\"name\": \"0.9.2342.19200300.100.1.1\", \"remarkEn\": \"Citizen Identity\", \"remark\": \"Căn cước công dân\", \"prefix\": \"CCCD:\", \"require\": true, \"attributeType\": \"TEXT_FIELD\"}, {\"name\": \"0.9.2342.19200300.100.1.1\", \"remarkEn\": \"Unique Identifier (Passport ID)\", \"remark\": \"Hộ chiếu\", \"prefix\": \"HC:\", \"require\": true, \"attributeType\": \"TEXT_FIELD\"}]}, {\"name\": \"CN\", \"remarkEn\": \"Personal Name\", \"remark\": \"Tên cá nhân\", \"require\": true, \"attributeType\": \"TEXT_FIELD\", \"commomNameType\": \"PERSON\"}, {\"name\": \"O\", \"remarkEn\": \"Organization\", \"remark\": \"Tổ chức\", \"require\": true, \"attributeType\": \"TEXT_FIELD\"}, {\"name\": \"OU\", \"remarkEn\": \"Organization Unit\", \"remark\": \"Đơn vị tổ chức\", \"require\": false, \"attributeType\": \"TEXT_FIELD\"}, {\"name\": \"T\", \"remarkEn\": \"Title\", \"remark\": \"Chức vụ\", \"require\": false, \"attributeType\": \"TEXT_FIELD\"}, {\"name\": \"E\", \"remarkEn\": \"Email Address in DN\", \"remark\": \"Địa chỉ Email\", \"require\": false, \"attributeType\": \"TEXT_FIELD\"}, {\"name\": \"telephoneNumber\", \"remarkEn\": \"Telephone Number\", \"remark\": \"Số điện thoại\", \"require\": false, \"attributeType\": \"TEXT_FIELD\"}, {\"name\": \"L\", \"remarkEn\": \"Locality\", \"remark\": \"Địa chỉ\", \"require\": false, \"attributeType\": \"TEXT_FIELD\"}, {\"name\": \"ST\", \"remarkEn\": \"State or Province\", \"remark\": \"Tỉnh/Thành phố\", \"require\": true, \"attributeType\": \"TEXT_FIELD\"}, {\"name\": \"C\", \"remarkEn\": \"Country (ISO 3166)\", \"remark\": \"Quốc gia (ISO 3166)\", \"require\": true, \"attributeType\": \"TEXT_FIELD\"}], \"attributeSans\": [{\"name\": \"rfc822Name\", \"remarkEn\": \"Email Address in SAN\", \"remark\": \"Địa chỉ Email trong SAN\", \"require\": false, \"attributeType\": \"TEXT_FIELD_SAN\"}]}";
        CERTIFICATION_TYPE_COMPONENT[][] resProfileData = new CERTIFICATION_TYPE_COMPONENT[1][];
        CommonFunction.getJsonComponentForCert(sCertProfileProperties, resProfileData);
        for(CERTIFICATION_TYPE_COMPONENT resProfileData1 : resProfileData[0])
        {
            System.out.println(EscapeUtils.CheckTextNull(resProfileData1.prefix));
        }*/
 /*String sDateIssuance = "07/12/2022";
        if (!"".equals(sDateIssuance)) {
            if (CommonFunction.checkDatePatternValid(sDateIssuance, Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY) == true) {
                if (CommonFunction.checkDateLesserCurrent(sDateIssuance, Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY) == false) {
                    System.out.println(Definitions.CONFIG_WS_RESPONSE_CODE_LEGAL_REPRESENTATIVE_TIME_INVALID);
                } else {
                    System.out.println("OK");
                }
            } else {
                System.out.println(Definitions.CONFIG_WS_RESPONSE_CODE_LEGAL_REPRESENTATIVE_TIME_INVALID);
            }
        }*/
//        int numerator = 390;
//        int denominator = 30;
//        int sss = numerator / denominator;
//        System.out.println("A: "+ sss);
//        String beforeDate = EscapeUtils.CheckTextNull("2021-11-18");
//        String laterDate = "";
//        if(!"".equals(beforeDate)) {
//            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//            SimpleDateFormat sdf2 = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY);
//            laterDate = sdf2.format(sdf1.parse(beforeDate));
//        }
//        System.out.println(laterDate);
//        LoadParamSystem.updateParamSystem(Definitions.CONFIG_PARAM_PREFIX_PERSONAL_CODE, Definitions.CONFIG_PREFIX_PERSONAL_CODE);
//        LoadParamSystem.updateParamSystem(Definitions.CONFIG_PARAM_PREFIX_PERSONAL_PASSPORT_CODE, Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE);
//        LoadParamSystem.updateParamSystem(Definitions.CONFIG_PARAM_PREFIX_PERSONAL_CITIZEN_CODE, Definitions.CONFIG_PREFIX_PERSONAL_CITIZEN_CODE);
//        LoadParamSystem.updateParamSystem(Definitions.CONFIG_PARAM_PREFIX_ENTERPRISE_TAX_CODE, Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE);
//        LoadParamSystem.updateParamSystem(Definitions.CONFIG_PARAM_PREFIX_ENTERPRISE_BUDGET_CODE, Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE);
//        LoadParamSystem.updateParamSystem(Definitions.CONFIG_PARAM_PREFIX_ENTERPRISE_DECISION, Definitions.CONFIG_PREFIX_ENTERPRISE_DECISION);
//        CERTIFICATION[][] rs = new CERTIFICATION[1][];
//        db.S_BO_CERTIFICATION_GET_INFO("28322", "1", rs);
//        if (rs[0].length > 0) {
//            System.out.println(rs[0][0].BRANCH_ID);
//            System.out.println(rs[0][0].P_ID);
//            System.out.println(rs[0][0].TAX_CODE);
//            System.out.println(rs[0][0].SUBJECT);
//        }
    /* String SubjectDN = "CN=CÔNG TY CỔ PHẦN XÂY DỰNG KHOÁNG SẢN THANH XUÂN, O=CÔNG TY CỔ PHẦN XÂY DỰNG KHOÁNG SẢN THANH XUÂN, L=Thành phố Vinh, ST=Nghệ An, C=VN, 0.9.2342.19200300.100.1.1=MST:2902137528";
        String sEmailNew = CommonFunction.getEmailInDN(SubjectDN);
        System.out.println("sEmailNew new: " + sEmailNew);*/
//       String sCSR = "-----BEGIN CERTIFICATE REQUEST-----\n" +
//"MIICtDCCAZwCAQAwcTEVMBMGA1UEAwwMTmd1eWVuIFZhbiBBMRQwEgYDVQQLDAtD\n" +
//"b25nIFR5IEFCQzEUMBIGA1UECgwLQ29uZyBUeSBBQkMxCzAJBgNVBAYTAlZOMQ8w\n" +
//"DQYDVQQHDAZRdWFuIDExDjAMBgNVBAgMBVRQSENNMIIBIjANBgkqhkiG9w0BAQEF\n" +
//"AAOCAQ8AMIIBCgKCAQEA3HdjNJSJeinKdiKVCG2YlNwicoN+7ZJDRpEdauev/9Cd\n" +
//"YTauZMfXoALW+LZUxQv3pyE/m/dVR/suL29JfpPm9lrT5x1Njc4A1fcKHZUKS9Ug\n" +
//"kATjq7LvvX8KvLCyNJelnjKQwvXD7Mc43nmfHSaQzX5agdGSdR2T2g8oLcSlhEHs\n" +
//"7+7+UCbrIaWnRMgkO6JXrPNGXNK3TPqTPJRx1wP0NMXP5p+3VYOCiKaTM9nXamsn\n" +
//"wHkLKVBslqywepoD6lmtyt8SimQjQfmxaP+ASoRBz5UM6v7NJ8vLjJTq1kjiElhU\n" +
//"TMpoGXZlt3VZAnEdPE6n723685AvLrW6ga9JaIgr3wIDAQABMA0GCSqGSIb3DQEB\n" +
//"BQUAA4IBAQCw+Cr6z2GKYKFfSmp7JPwyRsiUNZm+q0P8Oo4DHGcIwl0ZFth+KCOM\n" +
//"nQ4qEVMAcg20lLkpThL2jjkPc3cMZdeIthR+Cv1Nuyp2hyq+qCG9ABAJJWrPkA1w\n" +
//"d4xMaNhYndSBWEWPKIEHD0fbLjBhRUQdxtcxbiLWwGI0RI38F/hkxUV4T5xAv6+z\n" +
//"nJq2fhBYXNdLlmMx6nIknR6oCsyQA5OimOsnw6OYbmL3W/aaJJ1EWBeYA4/UCFJL\n" +
//"O3/oGBGU2/3REJVjDNFFUMz6dq7Lg0tCTPrxol56fypFx1hz7YFOD46hCgT1XGJL\n" +
//"Fy5V3daMVYs9u7CPm4epePDYIdvxa59D\n" +
//"-----END CERTIFICATE REQUEST-----";
//       String sKeySizeCSR = CommonFunction.getKeySizeFromCSR(sCSR);
//        System.out.println("AAAA: " +sKeySizeCSR);
//        String[] returnDetail = new String[3];
//        String sDate = "01/11/2021";
//        CommonFunction.subDateTimeDetailDay(sDate + " 00:00:00", returnDetail);
//        System.out.println("A: " + returnDetail[1]);
//        int intLimit = 0;
//        CERTIFICATION[][] rsScan;
//        ConnectDatabase dbPhaseOne = new ConnectDatabase();
//        ConnectDbPhaseTwo dbPhaseTwo = new ConnectDbPhaseTwo();
//        GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
//        dbPhaseOne.S_BO_GENERAL_POLICY_LIST("1", rsPolicy);
//        if (rsPolicy[0].length > 0) {
//            for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
//                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DAYS_AUTO_REVOKE_AFTER_REVISION)) {
//                    intLimit = Integer.parseInt(rsPolicy1.VALUE);
//                    break;
//                }
//            }
//        }
//        rsScan = new CERTIFICATION[1][];
//        System.out.println("intLimit: " + intLimit);
//        dbPhaseTwo.S_BO_CERTIFICATION_LIST_REVISION_TO_REVOKE(intLimit, rsScan);
//        if (rsScan[0].length > 0) {
//            for (CERTIFICATION item : rsScan[0]) {
//                System.out.println("ABC: " + item.ID);
//            }
//        }
//        try {
////constructor of file class having file as argument  
//            File file = new File("D:\\setup win\\54010CE6AE7983FC3D45DB8A3A8926F7_0108237036-001.cer");
//            if (!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not  
//            {
//                System.out.println("not supported");
//                return;
//            }
//            Desktop desktop = Desktop.getDesktop();
//            if (file.exists()) //checks file exists or not  
//            {
//                desktop.open(file);              //opens the specified file  
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    String sCert = "MIICiDCCAi6gAwIBAgIQQ6Ue3Pq1D35NWg/TrKXiUjAKBggqhkjOPQQDAjBpMQswCQYDVQQGEwJWTjE7MDkGA1UECgwyVmlldG5hbSBHb3Zlcm5tZW50IEluZm9ybWF0aW9uIFNlY3VyaXR5IENvbW1pc3Npb24xHTAbBgNVBAMMFENTQ0EgZUhlYWx0aCBWaWV0bmFtMB4XDTIxMDgwNDA2NDQwNFoXDTIzMDgwNDA2NDQwNFowXTELMAkGA1UEBhMCVk4xGzAZBgNVBAoMEk1pbmlzdHJ5IG9mIEhlYWx0aDExMC8GA1UEAwwoRG9jdW1lbnQgU2lnbmVyIFZhY3hpbiBDZXJ0aWZpY2F0ZSAwMDAwMTBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IABGwYjQi8LtXaj/nelqYLiFIRMO2Z/0H6lc3gnhamzhz8+07K+7FuJpUUeDV2qu4W87WfJGWo/U9tJFtYa0mLMESjgcMwgcAwDAYDVR0TAQH/BAIwADAfBgNVHSMEGDAWgBSfCkZ/QM0B8Q/SUZ3mQ4eKKJljNTAzBgNVHSUELDAqBgwrBgEEAQCON49lAQMGDCsGAQQBAI43j2UBAgYMKwYBBAEAjjePZQEBMB0GA1UdDgQWBBTmDfYKIwF2RKu+b6jg5SHgjcNrbTArBgNVHRAEJDAigA8yMDIxMDgwNDA2NDQwNFqBDzIwMjIwMTMxMDY0NDA0WjAOBgNVHQ8BAf8EBAMCB4AwCgYIKoZIzj0EAwIDSAAwRQIgLwrYStbO5JPSSzxM7NGZIWEiysBOl9ePSpv8G4xxBJcCIQDfrkLrPG0OgwfjtRmep3RtGUY/N8sqzESD/PzvLi43sg==";
////        InputStream imageStream = Test.class.getClass().getResourceAsStream("/test/DSC_6283.jpg");
//        InputStream imageStream = new ByteArrayInputStream(sCert.getBytes());
//        Path path = Files.createTempFile("DSC_6283", ".cer");
//        try (FileOutputStream out = new FileOutputStream(path.toFile())) {
//            byte[] buffer = new byte[1024];
//            int len;
//            while ((len = imageStream.read(buffer)) != -1) {
//                out.write(buffer, 0, len);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//        openHomePage(path.toFile());
    //Desktop.getDesktop().open(path.toFile());
}

private static void openHomePage(File file) throws IOException, Exception {
        System.out.println("AAAA: " + file.getPath());
        // windown
//        Runtime.getRuntime().exec(new String[] {"rundll32", "url.dll,FileProtocolHandler", file.getPath()});
        // linux

//        Runtime rt = Runtime.getRuntime();
//        rt.exec("rundll32 url.dll,FileProtocolHandler " + "https://localhost:8443");
//        Runtime rt = Runtime.getRuntime();
//        Process m;
//        int c = 0;
//
////        m = rt.exec(file.getPath());// "abc.bat"
//        m = rt.exec(new String[]{file.getPath(), file.getName(), file.getName() + "a", "--all", "-i"});
//        InputStream bis = new BufferedInputStream(m.getInputStream());
//        while (c != -1) {
//            c = bis.read();
//            //printing something on console
//            System.out.print("#");
//        }// this while loop executes till the batch file ends executing
//        m = null;
//        bis.close();
    }
}
