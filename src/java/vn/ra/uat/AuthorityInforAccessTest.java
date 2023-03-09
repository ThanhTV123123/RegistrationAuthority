/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.X509CertImpl;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;
import sun.security.x509.Extension;
import vn.ra.process.GetFeatureCertificate2;

/**
 *
 * @author USER
 */
public class AuthorityInforAccessTest {

//    public boolean isExtAuthorityInfoAccess(Extension ext) {
//        Pattern re = Pattern.compile("\\bcaIssuers\\b", Pattern.CASE_INSENSITIVE);
//        Matcher m = re.matcher(ext.toString());
//        if (m.find()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    ;
//
//    public static List<String> getAuthorityInfoAccesssUrls(String text) {
//        List<String> containedUrls = new ArrayList<String>();
//        Pattern pattern = Pattern.compile(
//                "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
//                + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
//                + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
//                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
//        Matcher urlMatcher = pattern.matcher(text);
//        while (urlMatcher.find()) {
//            containedUrls.add(text.substring(urlMatcher.start(0),
//                    urlMatcher.end(0)));
//        }
//        return containedUrls;
//    }
//
//    ;
//    public static void main(String[] args) throws Exception {
//        String certstr = "MIIFRzCCBC+gAwIBAgIQVAEBBHt/atdbuoibXUVudDANBgkqhkiG9w0BAQUFADBD\n" +
//"MQswCQYDVQQGEwJWTjEfMB0GA1UECgwWU0FWSVMgVEVDSE5PTE9HWSBHUk9VUDET\n" +
//"MBEGA1UEAwwKVHJ1c3RDQSBHMTAeFw0xOTEwMjQxMDI5NTdaFw0yMDEwMjMxMDI5\n" +
//"NTdaMIGSMQswCQYDVQQGEwJWTjESMBAGA1UECAwJSMOgIE7hu5lpMRQwEgYDVQQK\n" +
//"DAtDVFkgQVBJIEFCQzETMBEGA1UEAwwKZXhhbXBsZS52bjEeMBwGCgmSJomT8ixk\n" +
//"AQEMDk1TVDoxMjM0MzMyMjIxMSQwIgYJKoZIhvcNAQkBFhV0aGFuaHR2QHRvbWlj\n" +
//"YWxhYi5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCvefOwxsKW\n" +
//"XBQqCVrat+G+4zFZcoGTJnv2HORke1WXTdOgGDE3yG1qrPj176r3TJ7NS+gdMeSm\n" +
//"m28JvuT6YLJIAvhMtiH8fYnGkbKSY+Tc3DC5Hp3G/jyRvixllTponbCyRbaRjHk3\n" +
//"wWcAu6BG08X5aka3Mu1ynIWmQYMpD28rT/+tD4AAby1WKgfI5bpNtsw1lnOh69wO\n" +
//"nP5WycW30Ani26BzeZLb97aVUTyi8HZ4G+MGEL1EnN0OPG26ubndeq+sptkvkJmJ\n" +
//"IA3rdR22SvEAmezUi6zMTAg1aiNDBiLcbW6+eWrDoG/D9POQPkqnkFmJfrQ5MP0q\n" +
//"Pe2wEj8g3kwLAgMBAAGjggHlMIIB4TAMBgNVHRMBAf8EAjAAMB8GA1UdIwQYMBaA\n" +
//"FH3Igg0OYjlm2PLgyQyMl1nSu2NzMFEGCCsGAQUFBwEBBEUwQzBBBggrBgEFBQcw\n" +
//"AYY1aHR0cDovL2NhLmZpcy5jb20udm46ODA4MC9lamJjYS9wdWJsaWN3ZWIvc3Rh\n" +
//"dHVzL29jc3AwLAYDVR0RBCUwI4EVdGhhbmh0dkB0b21pY2FsYWIuY29tggpleGFt\n" +
//"cGxlLnZuME8GA1UdIARIMEYwRAYLKwYBBAGB7QMBBAEwNTAzBggrBgEFBQcCARYn\n" +
//"aHR0cDovL2RpY2h2dWRpZW50dS5mcHQuY29tLnZuL2Nwcy5odG1sMB0GA1UdJQQW\n" +
//"MBQGCCsGAQUFBwMCBggrBgEFBQcDATCBjwYDVR0fBIGHMIGEMIGBoH+gfYZ7aHR0\n" +
//"cDovL2NhLmZpcy5jb20udm46ODA4MC9lamJjYS9wdWJsaWN3ZWIvd2ViZGlzdC9j\n" +
//"ZXJ0ZGlzdD9jbWQ9Y3JsJmlzc3Vlcj1DTj1UcnVzdENBJTIwRzEsTz1TQVZJUyUy\n" +
//"MFRFQ0hOT0xPR1klMjBHUk9VUCxDPVZOMB0GA1UdDgQWBBSSqMFr2ugWWh81uUd+\n" +
//"XBhTyu5CiDAOBgNVHQ8BAf8EBAMCBPAwDQYJKoZIhvcNAQEFBQADggEBABd+3wjv\n" +
//"jQjiIyQcnwlcvUmI6SZVO0YkCj2VCkQHeRjW6V6ycdKN/QhczqRzrzoPOp7SHPEb\n" +
//"XZmmHNNHE/a1Q95tgYUOwtCeJn+t/LarKHdUKW+w9c2mZq+UVfsQi9+9iGpedRfg\n" +
//"8IR+Ry33+VokN7hsBQaRPFAZJWRWmZHeqDjKM9lj9IQqsXCfp6j9efOiU6AlRtw+\n" +
//"BfGHbxo4SCD0Asp7452RX8Ad9akGw2Ar+xo6B2aRJcFf4lkHunRZMZh23IiBNBkl\n" +
//"HcdTA82KJ42Ch+jmhaD/bio0vpidcpxgRzUO6pkqP4W5hZSOnSVDqzLcr/CgmZ+V\n" +
//"rJhXKreLhmEQBto=";
//        String[] parseCert = new String[15];
//        GetFeatureCertificate2.parserCertificateComponent(certstr, parseCert);
//        if("0".equals(parseCert[0]))
//        {
//            System.out.println(parseCert[1]);
//            System.out.println(parseCert[2]);
//            System.out.println(parseCert[3]);
//            System.out.println(parseCert[4]);
//            System.out.println(parseCert[5]);
//            System.out.println(parseCert[6]);
//            System.out.println(parseCert[7]);
//            System.out.println(parseCert[8]);
//            System.out.println(parseCert[9]);
//            System.out.println(parseCert[10]);
//            System.out.println(parseCert[11]);
//            System.out.println(parseCert[12]);
//            System.out.println(parseCert[13]);
//            System.out.println(parseCert[14]);
//        }
////        AuthorityInforAccessTest rc = new AuthorityInforAccessTest();
////
////        try {
//////            File file = new File("D:\\Common Test\\ssl_01.cer");
//////            byte[] encCert = new byte[(int) file.length()];
//////            FileInputStream fis = new FileInputStream(file);
//////            fis.read(encCert);
//////            fis.close();
////
////            InputStream in = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(certstr));
////            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
////            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
////
////            X509CertImpl impl = (X509CertImpl) cert;
////            int extnum = 0;
////            if (cert.getNonCriticalExtensionOIDs() != null) {
////                for (String extOID : cert.getNonCriticalExtensionOIDs()) {
////                    Extension ext = impl.getExtension(new ObjectIdentifier(extOID));
////                    if (ext != null) {
////                        if (rc.isExtAuthorityInfoAccess(ext)) {
////                            System.out.println(rc.getAuthorityInfoAccesssUrls(ext.toString()));
////                            // System.out.println("#"+(++extnum)+": "+ ext.toString());
////                            // CA ISSUERS ARE HERE
////                        }
////                    }
////                }
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        };
//    }
}
