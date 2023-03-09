/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.util.io.pem.PemObject;
import vn.ra.process.CommonFunction;
import vn.ra.process.GetFeatureCertificate2;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author USER
 */
public class TestDate {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static void main(String[] args) throws ParseException, IOException, NoSuchAlgorithmException, Exception {
        String fileUploaded = "D:\\Programer\\Company\\TMS-RA\\BCY\\cms_tcc\\Prod\\DSBP3840055_SK.C06.01.csr";
        String strView = "";
        try {
            InputStream in = new FileInputStream(fileUploaded);
            if (Base64.isBase64(IOUtils.toByteArray(new FileInputStream(fileUploaded)))) {
                String sParse = new String(IOUtils.toByteArray(in));
                strView = "PEM: " + sParse;
            } else {
                byte[] prvkey = IOUtils.toByteArray(in);
                strView = "DER:" + Base64.encodeBase64String(prvkey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("A: " + strView);

//        String sCSR ="MIIDgjCCAmoCAQAwggE9MR4wHAYKCZImiZPyLGQBAQwOTVNUOjk5OTk5OTk1NTQxHzAdBgoJkiaJk/IsZAEBDA9DTU5EOjEyMzQ1Njc4OTAxEjAQBgNVBAMMCVBIQU0gVEhBTzFOMEwGA1UECgxFQ8OUTkcgVFkgQ+G7lCBQSOG6pk4gQ8OUTkcgTkdI4buGIFRJTiBI4buMQyBFRlkgVknhu4ZUIE5BTSAtIFRFU1QgOTk4MU4wTAYDVQQLDEVDw5RORyBUWSBD4buUIFBI4bqmTiBDw5RORyBOR0jhu4YgVElOIEjhu4xDIEVGWSBWSeG7hlQgTkFNIC0gVEVTVCA5OTgxDjAMBgNVBAwMBU5WIEtUMRUwEwYDVQQHDAxD4bqmVSBHSeG6pFkxEjAQBgNVBAgMCUjDoCBO4buZaTELMAkGA1UEBhMCVk4wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCvaWelEhGWS03qTWBZEcL/jMM+vQigvNnnQuCedgC54sUag2G24E05iKu7BYznvZ5osBBL+RG8AfZu0pYOgOr0FEg/4C90sjvHjWLNZCG87q421C0b2K3S2mJkQJ/o7sPRoDYDRwtrSet/mA3DRAtyOEf7hIwcr81k1m9TNk3VOpaquembyLtRuZuvcXmyWWGJNJNdihI59GbYxhA9GmTABkKDpIeG14Kjxrt+Mr+G0eH3QYvESeyGxke6DibQb0oBzkzG4dS8pBd4+ud0SX3hWHS04TWEPRgihHX6J7yBcf2oUlwxo7YqWRbOS1hVg+IsKT0QMy3S95LBz1G3Ds9FAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAAHIQ6yPHTuAWczG42c0iXTSsQOXhZ0TcwMqXWYOvWjeK1GUaCtAc4oPRlHEyqZrxIOY/VyuGMGIeGxvLJHBdUhzv8fLTP5LkBedDU4kwKaMd3cjhWK1g/ue6jtWeMwV2q0DK0kg42Xn5n2FSCEV0W2ia68yGF+y1ZsbafjJ9hRHrd/fVVtvOM8SmEwdN3VwzRo+me/yqc+hYkmYsiaBSz7ci6CNd5w+MFQR9J+m8fMuXzYD20E/89ph0XNYmSH5yiJRvLve0NOKX1KCDmexa5FxYTtHO/894FRTN9C8ph+JCljV5JhoUmcm0MD92z36k+Pae4d9eRbP5qF2o9RuO30=";
//        System.out.println(CommonFunction.getKeySizeFromCSR(sCSR));
//        String sCERT = "MIIEfzCCA2egAwIBAgIQVAEBBHWIo0PLW9BimnBu+TANBgkqhkiG9w0BAQsFADBD\n" +
//"MQswCQYDVQQGEwJWTjEfMB0GA1UECgwWU0FWSVMgVEVDSE5PTE9HWSBHUk9VUDET\n" +
//"MBEGA1UEAwwKVHJ1c3RDQSBHMTAeFw0yMDA4MTMwODM4NTlaFw0yMzA4MTMwODM4\n" +
//"NTlaMIGnMQswCQYDVQQGEwJWTjEaMBgGA1UECAwRVFAgSOG7kyBDaMOtIE1pbmgx\n" +
//"ITAfBgNVBAMMGMSQb8OgbiBRdWFuZyBUw6JtIC0gOC4xMzEjMCEGCgmSJomT8ixk\n" +
//"AQEME0NNTkQ6OTg2NTU1NXRhbWRxMDExITAfBgkqhkiG9w0BCQEWEnRhbWRxQG1v\n" +
//"YmlsZS1pZC52bjERMA8GA1UEFBMIMTIzNDU2NzgwggEiMA0GCSqGSIb3DQEBAQUA\n" +
//"A4IBDwAwggEKAoIBAQCVf98mKpEtTbBGJU5PmgWKFcFmYa0j269SWCA0McDoPnRc\n" +
//"8cWX5OuGLAk+z2QQ66A+75vHUGwNH8hN1Qr+JHQf/MyEYxlwTpnzb9C8mcdawXeT\n" +
//"0naA090l28p7nLOVFZ49NhdmzlakQiQrGQyxCepd9ymtHUV3ajXy8rgNKBEC1b6G\n" +
//"0aRyS3DK3m/FnvpsYI50CMgCna/unjIjfMsMkKfrkEEMZyPrQXX/9VPcC71Vk1n8\n" +
//"0RKj2qB66VhV4q1KGjdOa82/c+N7gyepa/UPP0QHonqgHH6Q/rcxpvdiJrC5shwN\n" +
//"18G5wq/ZlAHHUkVNzPj97hy7oteMViTGK6IFCkO7AgMBAAGjggEIMIIBBDAMBgNV\n" +
//"HRMBAf8EAjAAMB8GA1UdIwQYMBaAFH3Igg0OYjlm2PLgyQyMl1nSu2NzMB0GA1Ud\n" +
//"EQQWMBSBEnRhbWRxQG1vYmlsZS1pZC52bjBPBgNVHSAESDBGMEQGCysGAQQBge0D\n" +
//"AQQBMDUwMwYIKwYBBQUHAgEWJ2h0dHA6Ly9kaWNodnVkaWVudHUuZnB0LmNvbS52\n" +
//"bi9jcHMuaHRtbDA0BgNVHSUELTArBggrBgEFBQcDAgYIKwYBBQUHAwQGCisGAQQB\n" +
//"gjcKAwwGCSqGSIb3LwEBBTAdBgNVHQ4EFgQUZGqCYkN+rsKiRVEttaXRcosQ5pcw\n" +
//"DgYDVR0PAQH/BAQDAgTwMA0GCSqGSIb3DQEBCwUAA4IBAQCSTZUEt4JmpsJ/xhaE\n" +
//"wKER9yYauDD+bfYQmX26tTmiBBzaWENdabGPq0J7LOigoB/NMuRz36og2GkaiM7U\n" +
//"HWqPq/EQPDOQ0ZgFt7npwSk4JnFkjODXDvE1YyVmq7vkyxgqIUsuDFOr6gHKKSnw\n" +
//"QYG87/DBKS4nbuWG0htTDRgnlZ6FO2pK+ySr7Or0GiCDNQzv+kNCipKI7KLZ0M1r\n" +
//"m5uMFtcN82BHF+LMXosh/TE7E76MUnCtiiXQtS9tsuW8zcBpT9DfeAqcL3nI6Wd5\n" +
//"aNpTK3Ya95FLE9s2C21CEZBVJubImmvj9V+seyXevCpypowZ5+Y1aAkePUyCKoHe\n" +
//"Nxfd";
//        String[] parseCert = new String[15];
//        GetFeatureCertificate2.parserCertificateComponent(sCERT, parseCert);
//        if ("0".equals(parseCert[0])) {
//            System.out.println("version: " + parseCert[1]);
//            System.out.println("signatureAlgorithm: " + parseCert[2]);
//            System.out.println("signatureHashAlgorithm: " +parseCert[3]);
//            System.out.println("authorityInformationAccess: " + parseCert[4]);
//            System.out.println("keyUsage: " + parseCert[5]);
//            System.out.println("enhancedKeyUsage: " + parseCert[6]);
//            System.out.println("subjectKeyIdentifier: " + parseCert[7]);
//            System.out.println("authorityKeyIdentifier: " + parseCert[8]);
//            System.out.println("certificatePolicies: " + parseCert[9]);
//            System.out.println("crlDistributionPoints: " + parseCert[10]);
//            System.out.println("basicConstraints: " + parseCert[11]);
//            System.out.println("subjectAlternativeName: " + parseCert[12]);
//            System.out.println("thumbprintAlgorithm: " + parseCert[13]);
//            System.out.println("thumbprint: " + parseCert[14]);
//        }

        /*String sDate = "25/02/2022 10:10:00";
//        Date currentDate = new Date();
        Date parsedDate = dateFormat.parse(sDate);
        System.out.println(dateFormat.format(parsedDate));
        Calendar c = Calendar.getInstance();
        c.setTime(parsedDate);
//        c.add(Calendar.YEAR, 1);
//        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, 1095); //same with c.add(Calendar.DAY_OF_MONTH, 1);
//        c.add(Calendar.HOUR, 1);
//        c.add(Calendar.MINUTE, 1);
//        c.add(Calendar.SECOND, 1);
        Date currentDatePlusOne = c.getTime();
        System.out.println(dateFormat.format(currentDatePlusOne));*/
 /*String beforeDate = EscapeUtils.CheckTextNull("2018-12-18");
        String laterDate = "";
        if(!"".equals(beforeDate)) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY);
            laterDate = sdf2.format(sdf1.parse(beforeDate));
        }
        System.out.println("laterDate: " + laterDate);*/
    }
}
