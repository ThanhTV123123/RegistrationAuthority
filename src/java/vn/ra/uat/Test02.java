/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import vn.mobileid.esigncloud.management.OwnerInfo;
import vn.ra.object.ATTRIBUTE_VALUES;
import vn.ra.object.BRANCH;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_PROPERTIES_JSON;
import vn.ra.object.CredentialDataAuthen;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.JSON_USER_BRANCH_DEFAULT;
import vn.ra.object.PKI_FORMFACTOR;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.EncodeSOPIN;
import vn.ra.process.GetFeatureCertificate2;
import vn.ra.process.RSSPProcessCommon;
import vn.ra.utility.ConfigLog;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.PropertiesContent;

/**
 *
 * @author USER
 */
public class Test02 {

    public static void main(String[] args) throws Exception {
        /*String strJson = "{\"tokenSn\":\"54100000001007\",\"changeKeyEnabled\":true,\"keepCertificateSNEnabled\":false,\"revokeOldCertificateEnabled\":false,\"certtificateDeclineReason\":\"\",\"certtificateRevokeReason\":null,\"compromiseDate\":null,\"certtificateRevokeEJBCAReason\":null,\"deleteOldCertificateEnabled\":false,\"certRevokeDeleteInTokenEnabled\":false,\"certtificateSuspendReason\":null,\"promotionDuration\":\"0\",\"suspendedTime\":null,\"typeName\":\"ISSUE\",\"createUser\":\"phan xuan vu (vupx_admin)\",\"createDt\":1673516530728,\"approveUser\":\"Administrator (admin)\",\"approveDt\":1673516531238,\"approveCAUser\":\"Administrator (admin)\",\"approveCADt\":1673516531270,\"actionReason\":null,\"requestState\":\"APPROVED\",\"tokenDeclineReason\":null,\"tokenApproveRemark\":null,\"rsspAgreementUUID\":\"4009d2a0-c146-4443-a0c0-393ffa3d054f\",\"rsspRelyingParty\":\"BACKOFFICE_RP\",\"rsspCertificateUUID\":null,\"rsspConnectWSMode\":null,\"certificationAttrId\":null,\"otpAuthenticationEnabled\":false,\"otp\":null,\"otpExpiration\":null,\"attributeData\":{\"certificationData\":{\"pushNoticeEnable\":0,\"duration\":0,\"feeAmount\":0,\"tokenAmount\":0,\"personalName\":\"202301121641\",\"companyName\":\"\",\"enterpriseID\":\"\",\"personalID\":\"\",\"subjectDn\":\"CN=202301121641, O=test nhiều OU, OU=test 1, OU=test 1, OU=test 1, OU=test 1, T=staff, E=thoantk@mobile-id.vn, L=19 Đặng Tiến Đông, C=VN\",\"issuerSubject\":\"C=VN,O=Ban Cơ yếu Chính phủ,CN=CPCA chuyên dùng Chính phủ G2\",\"phoneContract\":\"0337767863\",\"emailContract\":\"thoantk@mobile-id.vn\",\"provinceName\":\"N/A\",\"typeName\":\"ISSUE\",\"tokenSn\":\"54100000001007\",\"pkiFromFactorId\":0,\"pkiFromFactorCode\":\"ESIGNCLOUD\"}},\"tokenIdOfBundleList\":null}";
        ObjectMapper objectMapper = new ObjectMapper();
        ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(strJson, ATTRIBUTE_VALUES.class);
        System.out.println("AA: " + valueATTR_Frist.getRevokeSetOldStatusEnabled());*/
        
        /*String sUserKeyNEAC = "glS0oHATDGQgHl9";
        String sDataSignature = "Type=1&CertSubject=2&UserID=ECA.Canbo&MD5CertHash=9b58687ab47c472073cc949a5021673327dd4bfe3a3eef617781550f439586e1&MD5PdfHash=2e61a170116d764be1d9f124487006981a8546f01e35eefe042524a5b6d92ed1&UserKey=glS0oHATDGQgHl9";
        String encodeSignature = CommonFunction.encodeHmacForNEAC(sUserKeyNEAC, sDataSignature);
        System.out.println("A: " + encodeSignature);
        
        String sFileContent1 = "MIIFUDCCBDigAwIBAgIUY/68Jtig5HfiseykKb4J1Bmj8TswDQYJKoZIhvcNAQELBQAwbzExMC8GA1UEAwwoRS1DQSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eSBDZW50ZXIgVGVzdDEaMBgGA1UECwwRRUNBIFRydXN0IE5ldHdvcmsxETAPBgNVBAoMCFRTRCBDT1JQMQswCQYDVQQGEwJWTjAeFw0yMzAyMTQwODQyMTJaFw0yMzAzMTQwODQyMTJaMIGzMQswCQYDVQQGEwJWTjEpMCcGA1UECB4gAFQAaADgAG4AaAAgAHAAaB7RACAASADgACAATh7ZAGkxGjAYBgoJkiaJk/IsZAEBEwowMTAxMzAwODQyMV0wWwYDVQQDHlQAQwDUAE4ARwAgAFQAWQAgAFQATgBIAEgAIABQAEgAwQBUACAAVABSAEkewgBOACAAQwDUAE4ARwAgAE4ARwBIHsYAIABUAEgAwQBJACAAUwGgAE4wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCpLa7L7GYSUZwcOEsmNdb0YxbkRJjgCvCQN/aip0qvP6xMURC572VypFM8ZGUg7RVYgw0SznKK6wmGNdyCQc7PvwiDEbLnSvLA53kpqz5RR3OkP4fM7Mpi1wKUVzVGA5E7NaiqVMsXNcyC9lz9KHYt5vl4zmX9Fmv0EwfriVVOvdliaDm8JEGDg8d3ZS54oE7zV2dPMR/AZK4JJsyQXUbENP7I+Zn0zPTb5gRUGSGyt5dv+KLn17JJcPN7UJBFfqV6kMK2ewotuOiDq3zd4RVRYzyvG/AD6irEQFcHwvDJHO65BcDmw3PbalipWLNeDB0jtAARQlJZJLE+7kG0UfmzAgMBAAGjggGdMIIBmTAMBgNVHRMBAf8EAjAAMB8GA1UdIwQYMBaAFMCtu7T8pZ7SMhAxWDEHqBBpY+3CMIGVBggrBgEFBQcBAQSBiDCBhTA3BggrBgEFBQcwAoYraHR0cDovL3B1YmxpYy5yb290Y2EuZ292LnZuL2NydC9taWNucmNhLmNydDAmBggrBgEFBQcwAoYaaHR0cHM6Ly9lY2EuY29tLnZuL2VjYS5jcnQwIgYIKwYBBQUHMAGGFmh0dHA6Ly9vY3NwLmVjYS5jb20udm4wPgYDVR0gBDcwNTAzBgsrBgEEAYHtAwEEATAkMCIGCCsGAQUFBwIBFhZodHRwczovL2VjYS5jb20udm4vY3BzMDEGA1UdJQQqMCgGCCsGAQUFBwMCBggrBgEFBQcDAwYIKwYBBQUHAwQGCCsGAQUFBwMIMC4GA1UdHwQnMCUwI6AhoB+GHWh0dHA6Ly9jcmwuZWNhLmNvbS52bi9FQ0EuY3JsMB0GA1UdDgQWBBSZ448THW05HoICx+4ygXdmCFp/TTAOBgNVHQ8BAf8EBAMCA/gwDQYJKoZIhvcNAQELBQADggEBAH6ZJ/HRU86JMyXC8QjGqODyf2titG/oAmerVdvjP3b7soQtqsWweMlKAkaJ3zxMmtYj4ZO1NP0Ls++o3qOGfpnhRCPwYoZdHPcefyrW3nsmg9aaQZ7bb0dteMeQ7p5tS/04mPCxFsOFmDFkW6cNY3r0Qc2RO0iLHAg2+rX7WA4BoYuuvotOZqO6TwxsYBjKWQK0J+gk50cIRczC/oy+hzVnSutpjvVGMf1pndFoE9ngr1j0LagN2k9Zo8xxFX+GqGk0dq2FTWDxMCK+gDyEiXKeIfffnPzbmq+PuV8TfTUh9Rb8S9WWE24gQOvy3ymuqdPBCYmzv8pRj7yRMIFU2kA=";
        String sFileName1 = "0101300842.cer";
        String sMD5File = CommonFunction.getMD5(sFileName1 + sFileContent1);
        System.out.println("CERT: " + sMD5File);
        
        String sFileContent2 = "JVBERi0xLjQKJb/3ov4KMSAwIG9iago8PCAvUGFnZXMgMiAwIFIgL1R5cGUgL0NhdGFsb2cgPj4KZW5kb2JqCjIgMCBvYmoKPDwgL0NvdW50IDEgL0tpZHMgWyAzIDAgUiBdIC9UeXBlIC9QYWdlcyA+PgplbmRvYmoKMyAwIG9iago8PCAvQ29udGVudHMgNCAwIFIgL0dyb3VwIDw8IC9DUyAvRGV2aWNlUkdCIC9JIHRydWUgL1MgL1RyYW5zcGFyZW5jeSAvVHlwZSAvR3JvdXAgPj4gL01lZGlhQm94IFsgMCAwIDQ0Ni4yNSA2MzEuNSBdIC9QYXJlbnQgMiAwIFIgL1Jlc291cmNlcyA1IDAgUiAvVHlwZSAvUGFnZSA+PgplbmRvYmoKNCAwIG9iago8PCAvRmlsdGVyIC9GbGF0ZURlY29kZSAvTGVuZ3RoIDI5ID4+CnN0cmVhbQp4nDNUMABCXUMgYWZsqGeqkJzLVcgVyAUANVQEjWVuZHN0cmVhbQplbmRvYmoKNSAwIG9iago8PCA+PgplbmRvYmoKeHJlZgowIDYKMDAwMDAwMDAwMCA2NTUzNSBmIAowMDAwMDAwMDE1IDAwMDAwIG4gCjAwMDAwMDAwNjQgMDAwMDAgbiAKMDAwMDAwMDEyMyAwMDAwMCBuIAowMDAwMDAwMzAwIDAwMDAwIG4gCjAwMDAwMDAzOTkgMDAwMDAgbiAKdHJhaWxlciA8PCAvUm9vdCAxIDAgUiAvU2l6ZSA2IC9JRCBbPGUwMjI5MzAzZmM2NjU1MDAwNGIxZTVhYmRmNzkwZTc5PjxlMDIyOTMwM2ZjNjY1NTAwMDRiMWU1YWJkZjc5MGU3OT5dID4+CnN0YXJ0eHJlZgo0MjAKJSVFT0YK";
        String sFileName2 = "temp.pdf";
        String sMD5File2 = CommonFunction.getMD5(sFileName2 + sFileContent2);
        System.out.println("PDF: " + sMD5File2);*/
        
//        CERTIFICATION[][] rsCERT = new CERTIFICATION[1][];        
//        ArrayList<CERTIFICATION> tempList = new ArrayList<>();
////        CERTIFICATION rs = new CERTIFICATION();
////        rs.ADDRESS="123";
////        tempList.add(rs);
//        rsCERT[0] = new CERTIFICATION[tempList.size()];
//        rsCERT[0] = tempList.toArray(rsCERT[0]);
//        if(rsCERT[0].length > 0) {
//            System.out.println("YES: " + rsCERT[0][0].ADDRESS);
//        } else {
//            System.out.println("NO");
//        }
        
        /*DateFormat dfDate1 = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
        DateFormat dfDate2 = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY);
        String date1 = "20/10/2020 11:10:00";
        String date2 = "22/11/2022 11:15:00";
        Date second = dfDate1.parse(date1);
        System.out.println(dfDate2.format(second));*/
        
//        String urlCalllback = "https://uat.crm.vinca.vn/api/rest/s1/vinca/vinca_callback_test";
//        String urlCalllback = "https://uat.crm.vinca.vn/api/rest/s1/vinca-entry/cert-requests/response";
        /*String urlCalllback = "https://portal.wgroup.vn/tms/log";
        JsonObject json = new JsonObject();
//        json.addProperty("certificateID", "12301");
        json.addProperty("certificateID", "12301");
        json.addProperty("functionType", "1");
        json.addProperty("tokenSN", "541111111111");
        CloseableHttpResponse response;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(urlCalllback);
            System.out.println("REQUEST: " + json.toString() + "; URL: " + urlCalllback);
            StringEntity entity = new StringEntity(json.toString());
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            response = client.execute(httpPost);
            client.close();
        }
        System.out.println("RESPONSE: " + response.toString());*/
        
//        System.out.println(dfDate.format(date1) + " - " + dfDate.format(date2));
        
//        ConnectDatabase db = new ConnectDatabase();
//        PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
//        db.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
//        String sFormFactorPro = "";
//        String certSN = "";
//        if(rsFormfactorPro[0].length > 0) {
//            String sVALUE = "{\"tokenSn\":\"54100000001005\",\"changeKeyEnabled\":true,\"keepCertificateSNEnabled\":false,\"revokeOldCertificateEnabled\":false,\"certtificateDeclineReason\":null,\"certtificateRevokeReason\":null,\"compromiseDate\":null,\"certtificateRevokeEJBCAReason\":null,\"deleteOldCertificateEnabled\":false,\"certRevokeDeleteInTokenEnabled\":false,\"certtificateSuspendReason\":null,\"promotionDuration\":null,\"suspendedTime\":null,\"typeName\":\"ISSUE\",\"createUser\":\"Thơm (thomlt)\",\"createDt\":1658300477190,\"approveUser\":\"Thơm (thomlt)\",\"approveDt\":1658300477247,\"approveCAUser\":\"Thơm (thomlt)\",\"approveCADt\":1658300477247,\"actionReason\":null,\"requestState\":\"APPROVED\",\"tokenDeclineReason\":null,\"tokenApproveRemark\":null,\"rsspAgreementUUID\":\"8562a035-f1a1-41cb-9ab1-09d060b2ff6b\",\"rsspRelyingParty\":\"vCSP\",\"rsspCertificateUUID\":\"220720140638497JCBCejOyuvTDDye6Tl\",\"rsspConnectWSMode\":\"SOAP\",\"certificationAttrId\":null,\"otpAuthenticationEnabled\":false,\"otp\":null,\"otpExpiration\":null,\"attributeData\":{\"certificationData\":{\"pushNoticeEnable\":0,\"duration\":0,\"feeAmount\":0,\"tokenAmount\":0,\"personalName\":\"\",\"companyName\":\"LƯU THỊ THƠM\",\"enterpriseID\":\"TIN:TEST20220722\",\"personalID\":\"\",\"subjectDn\":\"CN=LƯU THỊ THƠM, O=LƯU THỊ THƠM, OU=Tổ Chức Liên Hợp Quốc, E=luuthithom0812@gmail.com, telephoneNumber=0398968772, L=11 Xô Viết Nghệ Tĩnh\\\\, Phường 22\\\\, Quận Bình Thạnh, ST=Hồ Chí Minh, C=VN, 0.9.2342.19200300.100.1.1=MST:TEST20220722\",\"issuerSubject\":\"CN=Mobile-ID Trusted Services,O=Mobile-ID Technologies and Services Joint Stock Company,C=VN\",\"phoneContract\":\"0398968772\",\"emailContract\":\"luuthithom0812@gmail.com\",\"provinceName\":\"Hồ Chí Minh\",\"typeName\":\"ISSUE\",\"tokenSn\":\"54100000001005\",\"pkiFromFactorId\":4}},\"tokenIdOfBundleList\":null}";
//            String agreementUUID = "";
//            String relyingPartyName = "";
//            if(!"".equals(sVALUE)){
//                sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
//                CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
//                ObjectMapper objectMapper = new ObjectMapper();
//                ATTRIBUTE_VALUES valueRssp = objectMapper.readValue(sVALUE, ATTRIBUTE_VALUES.class);
//                agreementUUID = EscapeUtils.CheckTextNull(valueRssp.getRsspAgreementUUID());
//                relyingPartyName = EscapeUtils.CheckTextNull(valueRssp.getRsspRelyingParty());
//                String[] sParam = new String[3]; int[] sCode = new int[1];
//                RSSPProcessCommon rsspClass = new RSSPProcessCommon();
//                rsspClass.getCertificateDetailForSignCloud(credentialAuthen, relyingPartyName, agreementUUID, certSN, sParam, sCode);
//                if(sCode[0] == 0) {
//                    String remainingSigningCounter = sParam[1];
//                    if(remainingSigningCounter.equals("-1")){
//                        remainingSigningCounter = "UNLIMITED";
//                    }
//                    System.out.println(remainingSigningCounter);
//                }
//            }
//        }
        
        
        /*String sREGEX_DB = "REGEX_PHONE=^(0|1|84)[0-9]{4,16}\n"
                + "REGEX_EMAIL=^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$";
        String sREGEX_PHONE = PropertiesContent.getPropertiesContentKey(sREGEX_DB, Definitions.CONFIG_REGEX_PHONE);
        if ("".equals(sREGEX_PHONE.trim())) {
            sREGEX_PHONE = Definitions.CONFIG_DEFAULT_VALUE_REGEX_PHONE;
        }
        String phoneContact = " 0962457574 ";
        if (CommonFunction.regexPhoneValid(EscapeUtils.CheckTextNull(phoneContact), sREGEX_PHONE) == false) {
            System.out.println("FAILD");
        } else {
            System.out.println("OK");
        }*/

        //String pCSR = "";
        //String sPublicKeyHard = CommonFunction.getPublicKeyHasrCSR(pCSR);
//        System.out.println(sPublicKeyHard);
//        String sTYPE_OF_DATE = "1";
//        
//        final int intAMPM = "0".equals(sTYPE_OF_DATE) ? Calendar.PM : Calendar.AM;
//        Calendar calendar = null;
//        calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, -1);
//        calendar.set(Calendar.HOUR, 9);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        calendar.set(Calendar.AM_PM, intAMPM);
//        java.sql.Timestamp pFROM_DT = new Timestamp(calendar.getTimeInMillis());
//        calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, 0);
//        calendar.set(Calendar.HOUR, 9);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        calendar.set(Calendar.AM_PM, intAMPM);
//        java.sql.Timestamp pTO_DT = new Timestamp(calendar.getTimeInMillis());
//        System.out.println(pFROM_DT);
//        System.out.println(pTO_DT);
        // REMOVE FONT UNICODE
        /*String sValue = "abc êm.pdf";
        String sConvert = cleanString(sValue);
        System.out.println(sConvert);*/
//        String[] sResult = new String[2];
        //RSSPProcessCommon rs = new RSSPProcessCommon();
//        List<OwnerInfo> rss = rs.getOwnerInfoForSignCloud("", "thanhtv@tomicalab.com", "", "", "", "", "", sResult);
//        System.out.println(sResult[0]);
//        List<String> listAuthModes = rs.getAuthModesRSSP();
//        if (listAuthModes.size() > 0) {
//            for (String rpAuthen : listAuthModes) {
//                System.out.println(rpAuthen);
//            }
//        }
//        List<String> listRelyingParty = rs.getRelyingPartiesRSSP();
//        if (listRelyingParty.size() > 0) {
//            for (String rpRelying : listRelyingParty) {
//                System.out.println(rpRelying);
//            }
//        }
        String sCert = "MIIE+DCCA+CgAwIBAgIQVAEBBBgAzd4cUq34JcOhTTANBgkqhkiG9w0BAQUFADBD\n"
                + "MQswCQYDVQQGEwJWTjEfMB0GA1UECgwWU0FWSVMgVEVDSE5PTE9HWSBHUk9VUDET\n"
                + "MBEGA1UEAwwKVHJ1c3RDQSBHMTAeFw0xOTEyMDIxMDAzMzdaFw0yMTEyMDEwOTEw\n"
                + "MDBaMF0xCzAJBgNVBAYTAlZOMRowGAYDVQQIDBFUUCBI4buTIENow60gTWluaDER\n"
                + "MA8GA1UEAwwIdGVzdC5jb20xHzAdBgoJkiaJk/IsZAEBDA9NU1Q6MDEwMTE0ODE1\n"
                + "MzMwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCPiwK+dE47IHFfBlQe\n"
                + "OcPEXoHzbWoiSvcCh6DJNt6t29j6H8n+iy1MbS62qsAFVEytoEWli3ltaX7ypjrd\n"
                + "LX1NLSbYHD/mvjSOy8SZfd21Yz/VLSa7ebJZH/3GWnVHOlEF8nCqGwfx/7h2oZ/q\n"
                + "t6TuwqAEboraxj1yoLAN+14Z3oSx7tQI0zginEXZYzKBAfqkeH1br5h+eWI9CY34\n"
                + "KY9aoGYB4VWiaFDPG4v50RkfwLrpbw9/7qRDbCqP3yFMVNK5DrS5EiuBPlxfxFun\n"
                + "xcA/WerdUdZ2jdabeEM0Gr3z2R6BiRVRpXEepn6GNecm7+C1Zo2VYELm+4xGKE65\n"
                + "Tl8/AgMBAAGjggHMMIIByDAMBgNVHRMBAf8EAjAAMB8GA1UdIwQYMBaAFH3Igg0O\n"
                + "Yjlm2PLgyQyMl1nSu2NzMFEGCCsGAQUFBwEBBEUwQzBBBggrBgEFBQcwAYY1aHR0\n"
                + "cDovL2NhLmZpcy5jb20udm46ODA4MC9lamJjYS9wdWJsaWN3ZWIvc3RhdHVzL29j\n"
                + "c3AwEwYDVR0RBAwwCoIIdGVzdC5jb20wTwYDVR0gBEgwRjBEBgsrBgEEAYHtAwEE\n"
                + "ATA1MDMGCCsGAQUFBwIBFidodHRwOi8vZGljaHZ1ZGllbnR1LmZwdC5jb20udm4v\n"
                + "Y3BzLmh0bWwwHQYDVR0lBBYwFAYIKwYBBQUHAwIGCCsGAQUFBwMBMIGPBgNVHR8E\n"
                + "gYcwgYQwgYGgf6B9hntodHRwOi8vY2EuZmlzLmNvbS52bjo4MDgwL2VqYmNhL3B1\n"
                + "YmxpY3dlYi93ZWJkaXN0L2NlcnRkaXN0P2NtZD1jcmwmaXNzdWVyPUNOPVRydXN0\n"
                + "Q0ElMjBHMSxPPVNBVklTJTIwVEVDSE5PTE9HWSUyMEdST1VQLEM9Vk4wHQYDVR0O\n"
                + "BBYEFJDZjPH5xlegWKVymQE+t29Ylcb1MA4GA1UdDwEB/wQEAwIE8DANBgkqhkiG\n"
                + "9w0BAQUFAAOCAQEAC02EUClkZ1gBPvLN5gisAm6nXRSA1RJ6YMlGyp8Oz4YLP6oC\n"
                + "vHC6EHyhhlbOQhNdCuom2KOVNWls5e7I2CYI5grU16SjCkhBnOMGqY05scz/9wNK\n"
                + "nhxjGwo+FPgOuvvCzEyVCLlofWs5Ui5W+tPkD4wu/BrXISh/o8GNPkFTmWxz9Lnd\n"
                + "YI2RZBw5PpDjwp4jsm3Cmh1Zi1PYhIWGvfQ+G+Kgzf7cutpxBbc9z1HKoevSWpOp\n"
                + "+RII4fKZiI1kGhRXOPvMmyJcpomrxjWRC1ejN0PMcStnZE17y2GPQZYwH2B9gltD\n"
                + "o2g8ZrYBrvVAlJFstwB0wF6NyHeiKM6FOZwnRw==";
//        String[] sComponent = new String[15];
//        GetFeatureCertificate2.parserCertificateComponent(sCert, sComponent);
//        if("0".equals(sComponent[0]))
//        {
//            System.out.println(sComponent[12]);
//        }

//        boolean scheck = CommonFunction.checkAgencyAccessMenuID(10);
//        System.out.println(scheck);
//        String sExpirationTime = "30/10/2021 10:10:01";
//        java.sql.Timestamp pEFFECTIVE_DT = CommonFunction.ConvertStringToTimeStamp("20/10/2020 10:10:01");
//        java.sql.Timestamp pEXPIRATION_DT = null;
//        if(CommonFunction.checkExpirationWithProfile(sExpirationTime, pEFFECTIVE_DT, 365, 0,
//            Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY) == true) {
//            System.out.println("OK");
//            pEXPIRATION_DT = CommonFunction.ConvertStringToTimeStamp(sExpirationTime);
//        }
//        System.out.println(pEXPIRATION_DT);
//        Date currentDate = new Date();
//        java.sql.Timestamp pEFFECTIVE_DT = new java.sql.Timestamp(currentDate.getTime());
//        System.out.println(pEFFECTIVE_DT);
        //System.out.println(CommonFunction.remainDayWithCurrentTime("20/11/2021 10:10:01", Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY));
//        String sDNResult = "CN=Tran Van A, E=baotrv6@gmail.com, telephoneNumber=0985160831, L=19A\\\\, Dang Tien Dong\\\\, Q2, ST=TP Hồ Chí Minh, C=VN";
//        String sLocation = CommonFunction.getLocationInDN(sDNResult).trim();
//        System.out.println(sLocation);
//        
//        String pCSR = "-----BEGIN NEW CERTIFICATE REQUEST-----\n"
//                + "MIIDHzCCAgcCAQAwgdsxHjAcBgoJkiaJk/IsZAEBDA5DTU5EOjAyMzk0MDUyODEjMCEGA1UEAwwaaHXhu7NuaCBuZ3V54buFbiBnaWEgaMawbmcxIzAhBgkqhkiG9w0BCQEWFGdpYW5naG5AbW9iaWxlLWlkLnZuMRQwEgYDVQQUEwswOTAzMDQzMjA4ODEwMC4GA1UEBwwnMTk2YSB2xrDhu51uIGzDoGkgcC5hbiBwaMO6IMSRw7RuZyBxLjEyMRowGAYDVQQIDBFUUCBI4buTIENow60gTWluaDELMAkGA1UEBhMCVk4wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDvnlSWsawlwLOgte9EDTleUHw94Qyhd6LOOkVtc+4P9cq1rsH3x1XgSyxyK0VIczBeB02SmjfolCoT5mUuwmGztkKJjH47MNJfjdZqk1rpN1utgZYOZanUmMKuzm17ELvFglP930HsQ8VjFm0Mr45qaMl5e5V/Cgz1lasf/7pxi5tT8UcjCIfFKvP98VD2byNyiJmV29q/Bsy9/XmgEiRrje6aREK0EcaBuEqfQZM/xSa0Y0swvmlDWCoY93qvz7Hm/HED3XrPuLMZvMyHIz2hXuixRfUWspBpuyBS58csg0M1a6yzZNkt0jkSRGaRyKh2ukyXGaRL6+BFBM68gyslAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAGnLvDKOrnvE1zFMbfIYH0yJdFxhV5sBReync1vO7d4rSVyfTcp7kF4PM1oVDOG5gPLTMp1MPj31+ZdyINHb5i9uwVHhyQeVRp/c+8NpGL7VNJsingBokGBHBFiqCsN2DzUgp5jxZVGPYw4HnHJjN7yBtgqCsv9eA65R+7rT/CfFifQU0N28Mi1LvABpmjhgJWsBW/NuZGv7DlK7ep5ta/pSeD/P39gFwvdmF7QqPXlpTtzf44UUU/2sB9sjiYhr5b44u8PDSVFB1YnHy2Nomoz4HZU6NllEVQDHc393hOK2t5LwMfl9mDuYA1Mt9wl75bjSe0XhSV3zcftVRwjriS8=\n"
//                + "-----END NEW CERTIFICATE REQUEST-----";
////        String sKeySizeCSR = CommonFunction.getKeySizeFromCSR(pCSR);
////        System.out.println(sKeySizeCSR);
//
//        String sVALUE_OLD = "{\"tokenSn\":\"1A00272114300913\",\"typeName\":\"RENEWAL\",\"createUser\":\"vupx user (vupx_user)\",\"createDt\":1589345897781,\"requestState\":\"PENDING\",\"attributeData\":{\"certificationData\":{\"pushNoticeEnable\":true,\"certificationProfileId\":5,\"duration\":0,\"feeAmount\":0,\"tokenAmount\":0,\"personalName\":\"Vũ 202005131128\",\"companyName\":\"MobileID 202005131128\",\"budgetcode\":\"202005131128\",\"subjectdn\":\"e=vupx@tomicalab.com, 0.9.2342.19200300.100.1.1=CCCD:202005131128, 0.9.2342.19200300.100.1.1=MNS:202005131128, CN=Vũ 202005131128, O=MobileID 202005131128, ST=TP Hồ Chí Minh, C=VN\",\"issuerSubject\":\"C=VN,ST=Ho Chi Minh,L=Ho Chi Minh,O=Mobile-ID Technologies and Services Joint Stock Company,OU=Mobile-ID Technical Department,CN=Mobile-ID Trusted Network\",\"provinceName\":\"TP Hồ Chí Minh\",\"oldSerialNumber\":\"540101042F26DC0AC66C2305F4FCD2A5\"}},\"changeKeyEnabled\":false,\"revokeOldCertificateEnabled\":true,\"deleteOldCertificateEnabled\":true,\"otpAuthenticationEnabled\":false}";
//        ObjectMapper objectMapper = new ObjectMapper();
//        ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
//        String sLockReason = valueATTR_Frist.getTokenSn();
//        
//        System.out.println(sLockReason);
//        ObjectMapper objectMapper = new ObjectMapper();
//        ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
//        System.out.println(valueATTR_Frist.getActionReason());
        /*String sSCODE = "viettel-ca";
        EncodeSOPIN encript = new EncodeSOPIN();
        String strSOPIN = encript.encode(sSCODE);
        System.out.println(strSOPIN);
         */
 /*String s1 = "call S_TOOL_TOKEN_IMPORT('54081404111885','QT47Fz+1tnoOIzSWzbJ8ww==',2,2,1,'4be238bb-21c7-4b0d-8620-23faa1f7b1af');\n" +
            "DELIMITER ;\n" +
            "call S_TOOL_TOKEN_IMPORT('54081404111886','QT47Fz+1tnoOIzSWzbJ8ww==',2,2,1,'4be238bb-21c7-4b0d-8620-23faa1f7b1af');\n" +
            "DELIMITER ;\n" +
            "call S_TOOL_TOKEN_IMPORT('54081404111887','QT47Fz+1tnoOIzSWzbJ8ww==',2,2,1,'4be238bb-21c7-4b0d-8620-23faa1f7b1af');\n" +
            "DELIMITER ;";
        ConnectDatabase db = new ConnectDatabase();
        Connection conns = db.OpenDatabase();
        Statement statement = conns.createStatement();
        statement.execute(s1);
        Connection[] temp_connection = new Connection[]{conns};
        db.CloseDatabase(temp_connection);
         */
//       DecimalFormat formatter = new DecimalFormat("###,###,###");
//       System.out.println(formatter.format(1000000));
        /*String aTime = "20/20/2020 11:01:22";
        try {
            Date date = null;
            if(!"".equals(aTime)){
                DateFormat dfDate = new SimpleDateFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                date = dfDate.parse(aTime);
                System.out.println(date.toString());
            }else {System.out.println("NULL");}
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
//        String sArrayFormat = "JPG";
//        String sFilename = "aaasas.jahsbc.jpG";
//        boolean checkAllow = checkExtentionFileEnabled(sFilename, sArrayFormat);
//        System.out.println(checkAllow);
//        String str1 = "Thanh đe the";
//        System.out.println(CommonFunction.checkUnicodeFontString(str1));

        /*ConfigLog cnf = new ConfigLog();
        System.out.println(cnf.GetPropertybyCode("log"));*/
//        String sDateFrist = "2020-09-08 10:55:11.0";
//        String sDateInput = "01/04/2020 15:10:11";
//        System.out.println(CommonFunction.checkDateBiggerRequest(sDateFrist, sDateInput, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS));
//        System.out.println(CommonFunction.checkDateBiggerCurrent(sDateFrist, Definitions.CONFIG_DATETIME_FORMAT_YYYYDDMMHHMMSS));
//        String sValueUserBranch = "{ \"attributes\": [ { \"MSISDN\": \"0908775901\", \"EMAIL\": \"thanhtv@tomicalab.com\", \"REMARK\": \"Branch Default\", \"PARENT_NAME\": \"DL_01\", \"BRANCH_ROLE_NAME\": \"PROFILE_01\", \"PROVINCE_NAME\": \"\",\"attributeType\": \"BRANCH_DEFAULT_INFO\" },{ \"MSISDN\": \"0908775901\", \"EMAIL\": \"thanhtv@tomicalab.com\", \"REMARK\": \"User Default\", \"PARENT_NAME\": \"\", \"BRANCH_ROLE_NAME\": \"\", \"PROVINCE_NAME\": \"\", \"attributeType\": \"USER_DEFAULT_INFO\" } ] }";
//        JSON_USER_BRANCH_DEFAULT[][] rsCertType = new JSON_USER_BRANCH_DEFAULT[1][];
//        CommonFunction.getJsonUserBranchDefault(sValueUserBranch, rsCertType);
//        for(JSON_USER_BRANCH_DEFAULT rsCertType1 : rsCertType[0])
//        {
//            System.out.println(rsCertType1.REMARK);
//        }
//        boolean checkValidExpiration = CommonFunction.checkDateBiggerCurrent("09/09/2020 14:09:04", Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
//        System.out.println(checkValidExpiration);
//        String sDate = "09/09/2021 10:23:01";
//        System.out.println(CommonFunction.checkDateAddDayCurrent(sDate, 362, Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY));
//        System.out.println("USER:DIR!:" + System.getProperty("user.dir"));
        //boolean isTokenCheck = CommonFunction.checkSNB("LCS2003A00000002");
//        String USERNAME_PATTERN = "^[a-zA-Z0-9._-]{3,12}$";
//        boolean isTokenCheck = CommonFunction.regexEmailValid("LCS2003A00000002", USERNAME_PATTERN);
        //System.out.println(isTokenCheck);
        //Calendar c = Calendar.getInstance(); 
        //System.out.println("The Current Date is:" + c.getTime()); 
//        ConnectDatabase db = new ConnectDatabase();
//        CERTIFICATION[][] rsPginToken = new CERTIFICATION[1][];
//        db.S_BO_CERTIFICATION_BRIEF_LIST_EXPORT("","","","","","","","05","2020","","0","0","","1",rsPginToken,"","0","");
//        if(rsPginToken[0].length > 0) {
//            for(CERTIFICATION item:rsPginToken[0]) {
//                System.out.println(item.COMPANY_NAME);
//            }
//        }
        /*String pkcs1SignatureServer = "CdEKznu60/yEZKgpsoXuLLmrHxVn6FIoqIeMkAKmXrn0YqOeOsxY497JG2VQHiQgHA7jum3wT2kC9ATrqgORcqVHt+2mWiGXhyVpqzFqGUFVZ/fGdO4oWMW7oMVLcDj+HXNrsiBy/tlUrvKMafeVc0yq7+HSvmbh8GSls6ycXgObzh8yuMq1vuUTwzn37Xl5vFfSgz3iWJ23YPXXWfeXsW/Rt8E3uIWB4PCKtyseP7K1ee4QgFsYQaHUEwATpL/dBG16g+i5Nnh6ZValjuYF+Jv8qBNNSZonJNIw+ToPHS6mUITcb+Q1vb+Iw+0rW/a4qOjNsvkEahWUtPi0cn3apw==";
        PublicKey publicKey = CommonFunction.getPublicKeyInPemFormat(pkcs1SignatureServer);
        if (publicKey != null) {
            System.out.println("OK");
        } else {
            System.out.println("FALED");
        }*/
//        String sDN = "0.9.2342.19200300.100.1.1=MST:123123221, CN=DN test 01, O=DN test 01, ST=TP Hồ Chí Minh, C=VN, ";
//        System.out.println(CommonFunction.subLastCharater(sDN));
        // Creating a list 
//        List<String> oddList = new ArrayList<>(); 
//        //add elements to the list
//        oddList.add("aaa");
//        oddList.add("bbb");
//        oddList.add("ccc");
//        oddList.add("sss");
//        //print the original list
//        System.out.println("Original List1:" + oddList.get(0));
//        System.out.println("Original List:" + oddList);
//        oddList.remove(0);
//        System.out.println("Original List:" + oddList);
//        System.out.println("Original List1:" + oddList.get(0));
    }

    public static String cleanString(String str) {
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("đ", "d").replaceAll("Đ", "D");
    }

    public static boolean checkExtentionFileEnabled(String sFileName, String sArrayFormat) {
        boolean isResult = false;
        String strFileExten = EscapeUtils.CheckTextNull(FilenameUtils.getExtension(sFileName));
        if (!"".equals(strFileExten)) {
            if (!"".equals(sArrayFormat)) {
                String[] pItemArray = sArrayFormat.split(";");
                for (String sPlitValue : pItemArray) {
                    if (strFileExten.toUpperCase().equals(sPlitValue.trim().toUpperCase())) {
                        isResult = true;
                        break;
                    }
                }
            }
        }
        return isResult;
    }
}
